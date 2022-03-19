package ca.dal.csci3130.quickcash.usermanagement;

import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.mockito.Mockito;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class SignupActivityJUnitTest {
    // Test cases
    // -------------------------------------------------------
    // Check for valid email via regex method
    // Check for valid name (first and last) via regex
    // Test for matching duplicate passwords
    // Test that the password validation regex works
    // Method to validate phone number with particular format

    static SignupActivity signupActivity;
    SignupActivity signupActivity1;

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Before
    public void setup() {
        signupActivity = new SignupActivity();
        signupActivity1 = Mockito.mock(SignupActivity.class);
        UserDAO userDAO = Mockito.mock(UserDAO.class);
        when(userDAO.add(any(UserInterface.class))).thenReturn(null);
        doNothing().when(signupActivity1).addUser(any());
    }

    // Check for valid email. isValidEmail()
    @Test
    public void checkEmailValid() {
        assertTrue(signupActivity.isValidEmail("misterwillywonka@dal.ca"));
        assertTrue(signupActivity.isValidEmail("samabarbanel1@gmail.com"));
        assertTrue(signupActivity.isValidEmail("captain.poopy.flick@proskates.ca"));
    }

    // Check for invalid email.
    @Test
    public void checkEmailInvalid() {
        assertFalse(signupActivity.isValidEmail("pedram@rocks@dal.ca"));
        assertFalse(signupActivity.isValidEmail("whoosie!whoo@minckle.mart.ca"));
    }

    // Check for valid name (first and last)
    @Test
    public void checkNameValid() {
        assertTrue(signupActivity.isValidName("Sam"));
        assertTrue(signupActivity.isValidName("Pedram"));
    }

    // Check for invalid name
    @Test
    public void checkNameInvalid() {
        assertFalse(signupActivity.isValidName(""));
        assertFalse(signupActivity.isValidName("!Safari"));
    }


    // Test for matching duplicate passwords. This method is going to a password from each field
    @Test
    public void checkPasswordsMatch() {
        assertTrue(signupActivity.passwordsMatch("password!!1", "password!!1"));
    }

    @Test
    public void checkPasswordsDoNotMatch() {
        assertFalse(signupActivity.passwordsMatch("password!!1", "thisisn'tthesamepassword!!1"));
    }

    @Test
    // Method to validate phone number with format XXX-XXX-XXXX
    public void checkValidPhoneNumber() {
        assertTrue(signupActivity.isValidPhoneNumber("782-123-4567"));
    }

    @Test
    public void checkIsNotValidPhoneNumber() {
        assertFalse(signupActivity.isValidPhoneNumber("782 123 4567"));
        assertFalse(signupActivity.isValidPhoneNumber("(782)-123-4567"));
    }

    @Test
    public void checkPasswordValid() {
        assertTrue(signupActivity.passwordValid("boogiewoogie#123"));
        assertTrue(signupActivity.passwordValid("slayerboy23"));
    }

    @Test
    public void checkPasswordInvalid() {
        assertFalse(signupActivity.passwordValid("short1"));
        assertFalse(signupActivity.passwordValid("nonumbersbutlongenough"));
    }

}
