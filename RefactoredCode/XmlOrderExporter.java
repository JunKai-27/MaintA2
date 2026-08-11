package RefactoredCode;

public class XmlOrderExporter extends AbstractOrderExporter {

    @Override
    protected String format(String orderId, String customerName, double finalAmount) {
        return "<order>"
                + "<orderId>" + escapeXmlText(orderId) + "</orderId>"
                + "<customerName>" + escapeXmlText(customerName) + "</customerName>"
                + "<finalAmount>" + finalAmount + "</finalAmount>"
                + "</order>";
    }
    
    private String escapeXmlText(String value) {
        if (value == null) {
            return "";
        }
        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}