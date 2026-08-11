package RefactoredCode;
/**
 * R2 - Third-Party Payment Integration (Wrapper / Adapter Pattern)
 *
 * This class adapts the third-party PaymentLibrary (which exposes
 * processCardTransaction(cardNumber, amount) -> transaction ID) so that it can
 * be used anywhere the existing PaymentMethod interface is expected.
 *
 * Why an Adapter is required:
 *   - PaymentLibrary is third-party code and must not be modified.
 *   - The existing Order / OrderProcessor classes only know how to work with
 *     the PaymentMethod interface (applyFee(double amount) -> double).
 *   - CardPaymentAdapter bridges this incompatibility: it implements
 *     PaymentMethod, and internally delegates to PaymentLibrary.
 *
 * Design notes:
 *   - Per R2, the old flat $5 card fee logic (CardPayment) is retired for
 *     real card transactions. The third-party library now performs payment
 *     processing itself, so no additional fee is layered on top here.
 *   - The transaction ID returned by the library is logged for traceability
 *     but does not alter the final order amount, keeping calculateFinalAmount()
 *     in Order.java fully unchanged and unaware of this integration.
 */
public class CardPaymentAdapter implements PaymentMethod {
    private final String cardNumber;
    private final PaymentLibrary paymentLibrary;

    public CardPaymentAdapter(String cardNumber) {
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Card number cannot be empty");
        }
        this.cardNumber = cardNumber;
        this.paymentLibrary = new PaymentLibrary();
    }

    @Override
    public double applyFee(double amount) {
        String transactionId = paymentLibrary.processCardTransaction(cardNumber, amount);
        System.out.println("Card payment processed. Transaction ID: " + transactionId);
        // No additional fee is applied; the third-party library now owns
        // card payment processing, replacing the previous $5 flat fee.
        return amount;
    }

    // Exposed for testing / logging purposes only
    public String getCardNumber() {
        return cardNumber;
    }
}
}
