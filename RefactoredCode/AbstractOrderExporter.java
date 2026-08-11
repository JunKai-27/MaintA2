package RefactoredCode;
public abstract class AbstractOrderExporter {
    public final String export(Order order) {
        validateOrder(order);
        return format(
                order.getOrderId(),
                order.getCustomer().getName(),
                order.calculateFinalAmount()
        );
    }
    private void validateOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
    }
    protected abstract String format(String orderId, String customerName, double finalAmount);
}