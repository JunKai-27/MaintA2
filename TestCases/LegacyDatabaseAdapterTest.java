package TestCases;

import org.junit.jupiter.api.Test;

import RefactoredCode.CardPayment;
import RefactoredCode.Customer;
import RefactoredCode.LegacyDatabase;
import RefactoredCode.LegacyDatabaseAdapter;
import RefactoredCode.Order;
import RefactoredCode.OrderProcessor;

import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * R4 - Legacy Database Compatibility
 * Verifies that OrderProcessor/Order continue to work unchanged (they only ever call
 * Database.save(order)), while LegacyDatabaseAdapter transparently routes that call to
 * the legacy insertRecord(String tableName, String recordData) API.
 */
public class LegacyDatabaseAdapterTest {

    private Customer customer;
    private Order order;
    private RecordingLegacyDatabase recordingLegacyDb;
    private LegacyDatabaseAdapter adapter;

    @BeforeEach
    public void setUp() {
        customer = new Customer("Ang Yu Yang", "0123458463");
        order = new Order("ORD100", customer, 250.0, new CardPayment());
        recordingLegacyDb = new RecordingLegacyDatabase();
        adapter = new LegacyDatabaseAdapter(recordingLegacyDb);
    }

    @Test
    public void testSaveOrderCallsInsertRecordOnLegacyDatabase() {
        // Confirms the adapter is actually invoked and delegates to insertRecord()
        adapter.save(order);
        assertTrue(recordingLegacyDb.insertRecordCalled, "insertRecord() must be called when save(order) is invoked.");
    }

    @Test
    public void testLegacyDatabaseCompatibility_TableNameAndRecordData() {
        // Confirms the OMS -> legacy translation preserves the correct table + order data
        adapter.save(order);

        assertEquals("orders", recordingLegacyDb.lastTableName);
        assertTrue(recordingLegacyDb.lastRecordData.contains("orderId=ORD100"));
        assertTrue(recordingLegacyDb.lastRecordData.contains("customer=Ang Yu Yang"));
        assertTrue(recordingLegacyDb.lastRecordData.contains("phone=0123458463"));
        assertTrue(recordingLegacyDb.lastRecordData.contains("amount=" + order.calculateFinalAmount()));
    }

    @Test
    public void testOrderProcessorStillUsesSaveUnchanged() {
        // Confirms R4 required zero changes to OrderProcessor/Order: the adapter is
        // injected via the existing Database interface exactly like MySQLDatabase/OracleDatabase.
        OrderProcessor processor = new OrderProcessor(adapter);
        processor.process(order);
        assertTrue(recordingLegacyDb.insertRecordCalled);
    }

    @Test
    public void testAdapterRejectsNullLegacyDatabase() {
        assertThrows(IllegalArgumentException.class, () -> new LegacyDatabaseAdapter(null));
    }

    // Test double standing in for the third-party LegacyDatabase so we can assert on the
    // exact tableName/recordData the adapter sends, without touching the real class.
    private static class RecordingLegacyDatabase extends LegacyDatabase {
        boolean insertRecordCalled = false;
        String lastTableName;
        String lastRecordData;

        @Override
        public void insertRecord(String tableName, String recordData) {
            this.insertRecordCalled = true;
            this.lastTableName = tableName;
            this.lastRecordData = recordData;
        }
    }
}
