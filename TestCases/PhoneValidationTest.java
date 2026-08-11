package TestCases;

import RefactoredCode.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class PhoneValidationTest {
	@Test
    public void testExactly10Digits_isAccepted() {
        Customer customer = new Customer("Ooi Jun Kai", "0123456789");
        assertEquals("0123456789", customer.getPhoneDigits());
    }

    @Test
    public void testNineDigits_isRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new Customer("Ooi Jun Kai", "012345678"));
    }

    @Test
    public void testElevenDigits_isRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new Customer("Ooi Jun Kai", "01234567890"));
    }

    @Test
    public void testGotLetters_isRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new Customer("Ooi Jun Kai", "01234A6789")); // Invalid letter 'A'
    }

    @Test
    public void testGotSpecialChar_isRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new Customer("Ooi Jun Kai", "0123&56789")); // // Invalid special char '&'
    }

    @Test
    public void testReadableErrorMessage() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Customer("Ooi Jun Kai", null)); // purposely leave the phone number empty
        assertEquals("Invalid phone number: phone number must contain exactly 10 digits with no letters or special characters",
        		exception.getMessage());
    }
}
