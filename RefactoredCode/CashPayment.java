package RefactoredCode;

public class CashPayment implements PaymentMethod {
    @Override
    public double applyFee(double amount) {
        return amount;
    }
}