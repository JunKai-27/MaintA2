package RefactoredCode;

import java.util.Map;

public class OMS {
    public static void main(String[] args) {

        // For Order Management System
        System.out.println("--- Order Management System ---");
        Database database = DatabaseFactory.createDB(); // Factory pattern, unchanged (F15)

        // R2: use the Adapter instead of the old flat-fee CardPayment
        PaymentMethod paymentMethod = new CardPaymentAdapter("4111111111111111");
        
        try {
        	// R6: requires phone number to be exactly 10 digits with no spaces, hyphens or other symbols.
        	Customer customer = new Customer("John", "0123458463"); // valid phone format, so order processing proceeds.
            Order order = new Order(
                    "ORD001",
                    customer,
                    200.0,
                    paymentMethod
            );

            // R5: use the logging decorator instead of the plain OrderProcessor
            OrderProcessor processor = new LoggingOrderProcessor(database);
            processor.process(order);
        }catch (IllegalArgumentException ex) {
        	System.out.println("Error: " + ex.getMessage());
        }
                
        try {
        	// R6: Throws error message and skip the order creation when phone format is invalid
        	System.out.print("\n--- R6 Phone Validation Demo ---");
        	Customer customer = new Customer("James", "012-3458463"); // Invalid phone with '-'. So, error thrown.
            Order order = new Order( // Order creation for ORD002 is skipped
                    "ORD002",
                    customer,
                    200.0,
                    paymentMethod
            );
            OrderProcessor processor = new LoggingOrderProcessor(database);
            processor.process(order);
        }catch (IllegalArgumentException ex) {
        	System.out.println("\nError: " + ex.getMessage()); // Error message is printed here!
        }
        
        //R3: export order data to CSV, JSON, and XML using the reusable export architecture
        System.out.println("\n--- R3 Order Export Demo ---");
        Customer exportCustomer = new Customer("Erwin Khaw", "0123456789");
        Order exportOrder  = new Order("ORD-R3-001", exportCustomer, 300.0, new CashPayment());
        OrderExportService exportService = new OrderExportService();
        Map<String, String> exportedOrders = exportService.exportAll(exportOrder);

        System.out.println("--- CSV Export ---");
        System.out.println(exportedOrders.get("CSV"));

        System.out.println("\n--- JSON Export ---");
        System.out.println(exportedOrders.get("JSON"));

        System.out.println("\n--- XML Export ---");
        System.out.println(exportedOrders.get("XML"));
        
        // For Vehicle Management System
        System.out.println("\n--- Vehicle Management System ---");
        Bicycle bicycle = new Bicycle();
        bicycle.processVehicle();

        Truck truck = new Truck();
        truck.processVehicle();
        truck.startEngine();
    }
}
