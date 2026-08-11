package RefactoredCode;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * R5 - Logging Without Code Modification (Decorator Pattern)
 *
 * LoggingOrderProcessor extends the existing OrderProcessor and adds logging
 * behaviour around it, without changing a single line of OrderProcessor.java,
 * Order.java, or Database.java (all Assignment 1 classes remain untouched).
 *
 * It overrides process(Order order) to:
 *   1. Delegate to the original OrderProcessor.process() (super.process()) so
 *      existing behaviour — discount, fee, printing summary, database.save —
 *      is fully reused, not reimplemented.
 *   2. After delegation, log a timestamp, order ID, and final amount, as
 *      required by R5.
 *
 * This is exposed as a drop-in replacement: anywhere an OrderProcessor is
 * used, a LoggingOrderProcessor can be substituted with no other code changes,
 * because it IS-A OrderProcessor (classic Decorator-by-inheritance, acceptable
 * here since OrderProcessor has no extracted interface in Assignment 1).
 */
public class LoggingOrderProcessor extends OrderProcessor {

    private static final DateTimeFormatter TIMESTAMP_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public LoggingOrderProcessor(Database database) {
        super(database);
    }

    @Override
    public void process(Order order) {
        super.process(order);
        logOrder(order);
    }

    private void logOrder(Order order) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        double finalAmount = order.calculateFinalAmount();

        System.out.println("[LOG] " + timestamp
                + " | OrderID: " + order.getOrderId()
                + " | FinalAmount: " + finalAmount);
    }
}
