package RefactoredCode;

public class Customer {
	//We put private final for each of the variables to acheive better encapsulation where these values can only be accessed through public getters and no longer can be changed after initialised.
    private final String name;
    private final String phone;
    
    public Customer(String name, String phone) {
    	//Validate b4 object creation (Here we separate the validation of each field out to acheive better SRP)
    	validateName(name);
    	validatePhone(phone);
    	
    	//Object creation after successful validation
        this.name = name;
        this.phone = phone;
    }

    //Public getters
    public String getName() {
        return name;
    }
    
    // Re-engineered - Directly returns the stored phone number because badly-formatted's phone already be rejected during new Customer creation
    public String getPhoneDigits() {
        return phone;
    }
    
    //Validation methods (All private cuz it will only be called locally during object creation)    
	private void validateName(String name) {
		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("Name cannot be empty");
		}
	}
	
	// Re-engineered - New validation rules for phone formatting 
	private void validatePhone(String phone) {
		/* 
	     * Meaning of the regular expression "^[0-9]{10}$"
	     * ^: Means the start of the string.
	     * [0-9]: Means the stored value must match any number between 0 to 9.
	     * {10}: Means it requires the previous rule (a number from 0 to 9) to repeat exactly 10 times.
	     * $: Means the end of the string.
	    */
		String phoneRegex = "^[0-9]{10}$"; //the regular expression used to check the phone formatting  
	    
		if (phone == null || !phone.matches(phoneRegex)) { // check for empty String and match the provided String with the regular expression
	        throw new IllegalArgumentException(
	            "Invalid phone number: phone number must contain exactly 10 digits with no letters or special characters"
	        ); //Directly reject the order by throwing an error message instead of continue the order processing.
	    }
	}
}