package RefactoredCode;

public class Order {
	//We put private final for each of the variables to acheive better encapsulation where these values can only be accessed through public getters and no longer can be changed after initialised.
    private final String orderId;
    private final Customer customer;
    private final double orderAmount;
    private final PaymentMethod paymentMethod;

    // Re-engineered - New variables are updated to be used to calculate the tiered-discount. Defined here to avoid Magic Number 
    private static final double TIER_1_MIN_AMOUNT = 100.0;
    private static final double TIER_1_MAX_AMOUNT = 200.0;
    private static final double TIER_2_MAX_AMOUNT = 500.0;
    private static final double TIER_1_DISCOUNT_RATE = 0.05;
    private static final double TIER_2_DISCOUNT_RATE = 0.10;
    private static final double TIER_3_DISCOUNT_RATE = 0.15;
    
    public Order(String orderId, Customer customer, double orderAmount, PaymentMethod paymentMethod) {
        //Validate b4 object creation (Here we separate the validation of each field out to achieve better SRP)
        validateOrderId(orderId);
        validateCustomer(customer);
        validateOrderAmount(orderAmount);
        validatePaymentMethod(paymentMethod);
        
        //Object creation after successful validation
        this.orderId = orderId;
        this.customer = customer;
        this.orderAmount = orderAmount;
        this.paymentMethod = paymentMethod;
    }
    
    // Public getters
    public String getOrderId() {
        return orderId;
    }
    public Customer getCustomer() {
        return customer;
    }
    public double getOrderAmount() {
		return orderAmount;
	}
	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}
	
	//Validation methods (All private cuz it will only be called locally during object creation)
	private void validateOrderId(String orderId) {
		if (orderId == null || orderId.trim().isEmpty()) {
			throw new IllegalArgumentException("Order ID cannot be empty");
		}
	}
	
	private void validateCustomer(Customer customer) {
		if (customer == null) {
			throw new IllegalArgumentException("Customer cannot be null");
		}
	}
	
	private void validateOrderAmount(double amount) {
		if (amount < 0) {
			throw new IllegalArgumentException("Order amount cannot be negative");
		}
	}
	
	private void validatePaymentMethod(PaymentMethod paymentMethod) {
		if (paymentMethod == null) {
			throw new IllegalArgumentException("Payment method cannot be null");
		}
	}
	
	// Re-engineered - New function to get the tiered discount rate based on the order amount.
    private double getDiscountRate() { //Private cuz it will only be called by calculateDiscountedAmount()
    	double discountRate = 0.0;
    	if (orderAmount >= TIER_1_MIN_AMOUNT && orderAmount <= TIER_1_MAX_AMOUNT) {
    		discountRate = TIER_1_DISCOUNT_RATE;
        } else if (orderAmount > TIER_1_MAX_AMOUNT && orderAmount <= TIER_2_MAX_AMOUNT) {
            discountRate = TIER_2_DISCOUNT_RATE;
        } else if (orderAmount > TIER_2_MAX_AMOUNT) {
            discountRate = TIER_3_DISCOUNT_RATE;
        }
    	return discountRate;
    	
    }
    
    // Re-engineered - New logic to calculate the discount amount 
    private double calculateDiscountedAmount() { //Private cuz it will only be called by calculateFinalAmount()
        double discountRate = getDiscountRate(); //get the discount rate based on the order amount tier

        double discountedAmount = orderAmount - (orderAmount * discountRate);
        
        return Math.round(discountedAmount * 100.0) / 100.0; //to keep the discounted amount in 2 decimal places cuz it is money
    }
        
    public double calculateFinalAmount() {
        double discountedAmount = calculateDiscountedAmount();
        double finalAmount = paymentMethod.applyFee(discountedAmount);
        return finalAmount;
    }
}
