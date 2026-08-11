package RefactoredCode;

import java.util.LinkedHashMap;
import java.util.Map;

public class OrderExportService {
    private final Map<String, AbstractOrderExporter> exporters;

    public OrderExportService() {
        exporters = new LinkedHashMap<>();
        exporters.put("CSV", new CsvOrderExporter());
        exporters.put("JSON", new JsonOrderExporter());
        exporters.put("XML", new XmlOrderExporter());
    }

    public String export(Order order, String format) {
        AbstractOrderExporter exporter = exporters.get(normalizeFormat(format));
        if (exporter == null) {
            throw new IllegalArgumentException("Unsupported export format: " + format);
        }
        return exporter.export(order);
    }

    public Map<String, String> exportAll(Order order) {
        Map<String, String> exportedOrders = new LinkedHashMap<>();
        for (Map.Entry<String, AbstractOrderExporter> entry : exporters.entrySet()) {
            exportedOrders.put(entry.getKey(), entry.getValue().export(order));
        }
        return exportedOrders;
    }

    private String normalizeFormat(String format) {
        if (format == null) {
            return "";
        }
        return format.trim().toUpperCase();
    }
}