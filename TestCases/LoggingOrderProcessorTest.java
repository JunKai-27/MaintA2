package TestCases;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import RefactoredCode.*;

/**
 * R5 - Logging Without Code Modification
 * Verifies LoggingOrderProcessor logs timestamp, order ID, and final amount,
 * while still delegating the actual processing (discount/fee calc, printing,
 * saving) to the original, untouched OrderProcessor.
 */
public class LoggingOrderProcessorTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    /** Simple in-memory fake Database so tests don't depend on MySQL/Oracle. */
    private static class FakeDatabase implements Database {
        List<Order> savedOrders = new ArrayList<>();

        @Override
        public void save(Order order) {
            savedOrders.add(order);
        }
    }

    @Test
    public void process_logsOrderIdAndFinalAmount() {
        FakeDatabase db = new FakeDatabase();
        LoggingOrderProcessor processor = new LoggingOrderProcessor(db);

        Customer customer = new Customer("Bob", "0123458463");
        PaymentMethod cash = new CashPayment();
        Order order = new Order("ORD200", customer, 50.0, cash);

        processor.process(order);

        String output = outContent.toString();
        assertTrue("Log should contain [LOG] marker", output.contains("[LOG]"));
        assertTrue("Log should contain order ID", output.contains("ORD200"));
        assertTrue("Log should contain final amount", output.contains("50.0"));
    }

    @Test
    public void process_stillSavesOrderToDatabase() {
        FakeDatabase db = new FakeDatabase();
        LoggingOrderProcessor processor = new LoggingOrderProcessor(db);

        Customer customer = new Customer("Carol", "0123458463");
        PaymentMethod cash = new CashPayment();
        Order order = new Order("ORD201", customer, 150.0, cash);

        processor.process(order);

        assertEquals(1, db.savedOrders.size());
        assertEquals("ORD201", db.savedOrders.get(0).getOrderId());
    }

    @Test
    public void process_stillPrintsOriginalOrderSummary() {
        FakeDatabase db = new FakeDatabase();
        LoggingOrderProcessor processor = new LoggingOrderProcessor(db);

        Customer customer = new Customer("Dan", "0123458463");
        PaymentMethod cash = new CashPayment();
        Order order = new Order("ORD202", customer, 20.0, cash);

        processor.process(order);

        String output = outContent.toString();
        assertTrue("Original OrderProcessor summary should still print",
                output.contains("Order: ORD202"));
    }
}
