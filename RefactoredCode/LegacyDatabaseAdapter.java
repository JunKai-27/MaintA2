// Adapter Pattern: lets OrderProcessor keep calling database.save(order) (unchanged),
// while transparently translating that call into the legacy system's
// insertRecord(String tableName, String recordData) API. Satisfies R4 without touching
// LegacyDatabase (third-party) or OrderProcessor/Order/Customer (Assignment 1 classes).
package RefactoredCode;

public class LegacyDatabaseAdapter implements Database {

    private static final String TABLE_NAME = "orders";

    private final LegacyDatabase legacyDatabase;

    public LegacyDatabaseAdapter(LegacyDatabase legacyDatabase) {
        if (legacyDatabase == null) {
            throw new IllegalArgumentException("LegacyDatabase cannot be null");
        }
        this.legacyDatabase = legacyDatabase;
    }

    @Override
    public void save(Order order) {
        String recordData = buildRecordData(order);
        legacyDatabase.insertRecord(TABLE_NAME, recordData);
    }

    // Converts the Order object's public data into the flat string format the
    // legacy insertRecord() call expects.
    private String buildRecordData(Order order) {
        return "orderId=" + order.getOrderId()
                + ",customer=" + order.getCustomer().getName()
                + ",phone=" + order.getCustomer().getPhoneDigits()
                + ",amount=" + order.calculateFinalAmount();
    }
}
