package TestCases;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import RefactoredCode.AbstractOrderExporter;
import RefactoredCode.CashPayment;
import RefactoredCode.CsvOrderExporter;
import RefactoredCode.Customer;
import RefactoredCode.JsonOrderExporter;
import RefactoredCode.Order;
import RefactoredCode.OrderExportService;
import RefactoredCode.XmlOrderExporter;

public class OrderExporterTest {

    private Order order;

    @BeforeEach
    public void setUp() {
        Customer customer = new Customer("Erwin Khaw", "0123456789");
        order = new Order("ORD-R3-001", customer, 300.0, new CashPayment());
    }

    @Test
    public void testCsvExportContainsRequiredOrderData() {
        AbstractOrderExporter exporter = new CsvOrderExporter();

        String exportedOrder = exporter.export(order);

        assertEquals("orderId,customerName,finalAmount\n\"ORD-R3-001\",\"Erwin Khaw\",270.0", exportedOrder);
    }

    @Test
    public void testJsonExportContainsRequiredOrderData() {
        AbstractOrderExporter exporter = new JsonOrderExporter();

        String exportedOrder = exporter.export(order);

        assertEquals("{\"orderId\":\"ORD-R3-001\",\"customerName\":\"Erwin Khaw\",\"finalAmount\":270.0}", exportedOrder);
    }

    @Test
    public void testXmlExportContainsRequiredOrderData() {
        AbstractOrderExporter exporter = new XmlOrderExporter();

        String exportedOrder = exporter.export(order);

        assertEquals("<order><orderId>ORD-R3-001</orderId><customerName>Erwin Khaw</customerName><finalAmount>270.0</finalAmount></order>", exportedOrder);
    }

    @Test
    public void testExportServiceExportsAllFormats() {
        OrderExportService exportService = new OrderExportService();

        Map<String, String> exportedOrders = exportService.exportAll(order);

        assertEquals(3, exportedOrders.size());
        assertTrue(exportedOrders.containsKey("CSV"));
        assertTrue(exportedOrders.containsKey("JSON"));
        assertTrue(exportedOrders.containsKey("XML"));
    }

    @Test
    public void testExportServiceRejectsUnsupportedFormat() {
        OrderExportService exportService = new OrderExportService();

        assertThrows(IllegalArgumentException.class, () -> exportService.export(order, "PDF"));
    }

    @Test
    public void testExporterRejectsNullOrder() {
        AbstractOrderExporter exporter = new CsvOrderExporter();

        assertThrows(IllegalArgumentException.class, () -> exporter.export(null));
    }
}