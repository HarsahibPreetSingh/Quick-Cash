
package ca.dal.csci3130.quickcash.usermanagement;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


import android.content.Intent;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.mockito.Mockito;

public class LoginActivityJUnitTest {

    static Intent currentActivity;
    static LoginActivity loginActivity;
    LoginActivity loginActivity1;

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Before
    public void setup() {
        loginActivity = new LoginActivity();
        loginActivity1 = Mockito.mock(LoginActivity.class);
        UserDAO userDAO = Mockito.mock(UserDAO.class);
        when(userDAO.add(any(UserInterface.class))).thenReturn(null);
        currentActivity = loginActivity.getCurrentActivity();
    }

    @AfterClass
    public static void tearDown() {
        System.gc();
    }

    @Test
    public void checkIfEmailIsValid() {
        assertTrue(loginActivity.validateEmail("abc123@dal.ca"));
    }

    @Test
    public void checkIfEmailIsInvalid() {
        assertFalse(loginActivity.validateEmail("abc.123dal.ca"));
    }

    @Test
    public void checkIfPasswordIsValid() {
        assertTrue(loginActivity.validatePass("Hooray123"));
    }

    @Test
    public void checkIfPasswordIsInvalid() {
        assertFalse(loginActivity.validatePass("notPass"));
    }
}

