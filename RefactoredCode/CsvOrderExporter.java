package RefactoredCode;

public class CsvOrderExporter extends AbstractOrderExporter {

    @Override
    protected String format(String orderId, String customerName, double finalAmount) {
        return "orderId,customerName,finalAmount\n"
                + escapeCsvText(orderId) + ","
                + escapeCsvText(customerName) + ","
                + finalAmount;
    }

    private String escapeCsvText(String value) {
        String escapedValue = value == null ? "" : value.replace("\"", "\"\"");
        return "\"" + escapedValue + "\"";
    }
}