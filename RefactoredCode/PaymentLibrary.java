// Third-Party Payment Library (Do NOT modify — provided by the company, per Assignment 2 Appendix)
package RefactoredCode;

public class PaymentLibrary {
    public String processCardTransaction(String cardNumber, double amount) {
        System.out.println("Processing payment: " + amount);
        return "TXN" + System.currentTimeMillis();
    }
}
