package RefactoredCode;

public class JsonOrderExporter extends AbstractOrderExporter {

    @Override
    protected String format(String orderId, String customerName, double finalAmount) {
        return "{"
                + "\"orderId\":\"" + escapeJsonText(orderId) + "\","
                + "\"customerName\":\"" + escapeJsonText(customerName) + "\","
                + "\"finalAmount\":" + finalAmount
                + "}";
    }
    private String escapeJsonText(String value) {
        if (value == null) {
            return "";
        }
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }
}