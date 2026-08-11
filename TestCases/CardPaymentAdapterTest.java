package TestCases;
import org.junit.Test;

import RefactoredCode.CardPaymentAdapter;
import RefactoredCode.Customer;
import RefactoredCode.Order;
import RefactoredCode.PaymentMethod;

import static org.junit.Assert.*;

/**
 * R2 - Third-Party Payment Integration
 * Verifies CardPaymentAdapter correctly delegates to PaymentLibrary and no
 * longer applies the old $5 flat fee.
 */
public class CardPaymentAdapterTest {

    @Test
    public void applyFee_doesNotAddFlatFee_amountUnchanged() {
        PaymentMethod payment = new CardPaymentAdapter("4111111111111111");
        double result = payment.applyFee(180.0);
        assertEquals(180.0, result, 0.0001);
    }

    @Test
    public void applyFee_withZeroAmount_returnsZero() {
        PaymentMethod payment = new CardPaymentAdapter("4111111111111111");
        double result = payment.applyFee(0.0);
        assertEquals(0.0, result, 0.0001);
    }

    @Test
    public void constructor_rejectsNullCardNumber() {
        assertThrows(IllegalArgumentException.class, () -> new CardPaymentAdapter(null));
    }

    @Test
    public void constructor_rejectsEmptyCardNumber() {
        assertThrows(IllegalArgumentException.class, () -> new CardPaymentAdapter("   "));
    }

    @Test
    public void order_withCardPaymentAdapter_calculatesFinalAmountWithoutExtraFee() {
        Customer customer = new Customer("Alice", "0123458463");
        PaymentMethod payment = new CardPaymentAdapter("4111111111111111");
        Order order = new Order("ORD100", customer, 300.0, payment);

        double finalAmount = order.calculateFinalAmount();
        assertEquals(270.0, finalAmount, 0.0001);
    }

    // Regression test for the double-charge bug found during R4/R2/R5
    // integration testing: Order.calculateFinalAmount() can legitimately be
    // called more than once per order (e.g. LoggingOrderProcessor calls it a
    // second time to build its log line). applyFee() must only charge the
    // card on the FIRST call and reuse the same transaction afterwards.
    @Test
    public void applyFee_calledTwiceOnSameOrder_onlyChargesCardOnce() {
        CardPaymentAdapter payment = new CardPaymentAdapter("4111111111111111");

        payment.applyFee(180.0);
        String firstTransactionId = payment.getTransactionId();

        payment.applyFee(180.0);
        String secondTransactionId = payment.getTransactionId();

        assertNotNull("A transaction ID must be recorded after the first charge.", firstTransactionId);
        assertEquals("A second calculateFinalAmount()-style call must NOT create a new "
                + "transaction, or the customer gets double-charged.",
                firstTransactionId, secondTransactionId);
    }
}