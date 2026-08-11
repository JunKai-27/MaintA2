package TestCases;

import RefactoredCode.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TieredDiscountPolicyTest {
	
	private Customer customer;
	
	// Will run first before any of the test cases is executed to initialize the Customer objects 
    @BeforeEach
    public void setUp() {
        customer = new Customer("Ang Yu Yang", "0123456789");
    }
	
	@Test
	public void testAmountBelow100_noDiscount() {
	    Order order = new Order("R1-001", customer, 99.99, new CashPayment());
	    assertEquals(99.99, order.calculateFinalAmount(), 0.0001);
	}
	
	@Test
    public void testAmount100_5PercentDiscount() {
        Order order = new Order("R1-002", customer, 100.0, new CashPayment());
        assertEquals(95.0, order.calculateFinalAmount(), 0.0001);
    }

    @Test
    public void testAmount200_5PercentDiscount() {
        Order order = new Order("R1-003", customer, 200.0, new CashPayment());
        assertEquals(190.0, order.calculateFinalAmount(), 0.0001);
    }
    
    @Test
    public void testAmount201_10PercentDiscount() {
        Order order = new Order("R1-004", customer, 201.0, new CashPayment());
        assertEquals(180.9, order.calculateFinalAmount(), 0.0001);
    }

    @Test
    public void testAmount500_10PercentDiscount() {
        Order order = new Order("R1-005", customer, 500.0, new CashPayment());
        assertEquals(450.0, order.calculateFinalAmount(), 0.0001);
    }

    @Test
    public void testAmount501_15PercentDiscount() {
        Order order = new Order("R1-006", customer, 501.0, new CashPayment());
        assertEquals(425.85, order.calculateFinalAmount(), 0.0001);
    }
    
}
