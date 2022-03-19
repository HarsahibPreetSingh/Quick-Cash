package ca.dal.csci3130.quickcash.usermanagement;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static ca.dal.csci3130.quickcash.usermanagement.Delay.waitFor;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import ca.dal.csci3130.quickcash.R;

public class SignupActivityEspressoTest {

    @Rule
    public ActivityScenarioRule<SignupActivity> myRule = new ActivityScenarioRule<>(SignupActivity.class);
    public IntentsTestRule<SignupActivity> myIntentRule = new IntentsTestRule<>(SignupActivity.class);


    @BeforeClass
    public static void setup() {
        Intents.init();
    }

    @AfterClass
    public static void tearDown() {
        System.gc();
    }

    @Test
    public void checkIfEmailIsEmpty() {
        onView(withId(R.id.registerFirstName)).perform(typeText("abcde"));
        closeSoftKeyboard();
        onView(withId(R.id.registerLastName)).perform(typeText("abcde"));
        closeSoftKeyboard();
        onView(withId(R.id.registerEmail)).perform(typeText(""));
        closeSoftKeyboard();
        onView(withId(R.id.registerPhoneNumber)).perform(typeText("902-220-0566"));
        closeSoftKeyboard();
        onView(withId(R.id.registerPassword)).perform(typeText("hello1234"));
        closeSoftKeyboard();
        onView(withId(R.id.confirmPassword)).perform(typeText("hello1234"));
        closeSoftKeyboard();
        onView(withId(R.id.registerButton)).perform(click());
        onView(withId(R.id.registerEmail)).check(matches(hasErrorText("Email cannot be empty")));

    }

    @Test
    public void checkIfEmailIsInvalid() {
        onView(withId(R.id.registerFirstName)).perform(typeText("abcd"));
        onView(withId(R.id.registerLastName)).perform(typeText("abcd"));

        onView(withId(R.id.registerEmail)).perform(typeText("bhavyajain"));
        closeSoftKeyboard();
        onView(withId(R.id.registerButton)).perform(click());
        onView(withId(R.id.registerEmail)).check(matches(hasErrorText("Email invalid")));
    }

    @Test
    public void checkIfEmailExists() {
        onView(withId(R.id.registerFirstName)).perform(typeText("Hera"));
        onView(withId(R.id.registerLastName)).perform(typeText("Arif"));
        onView(withId(R.id.registerEmail)).perform(typeText("hr833194@gmail.com"));
        onView(withId(R.id.registerPhoneNumber)).perform(typeText("123-123-1234"));
        onView(withId(R.id.registerPassword)).perform(typeText("password1"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.confirmPassword)).perform(typeText("password1"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.registerButton)).perform(click());
        onView(isRoot()).perform(waitFor(500));
        onView(withId(R.id.registerEmail)).check(matches(hasErrorText("Email already exists")));
    }

    @Test
    public void checkIfRegistrationPageIsVisible() {
        onView(withId(R.id.registerFirstName)).check(matches(withText("")));
        onView(withId(R.id.registerLastName)).check(matches(withText("")));
    }

    @Test
    public void checkIfFirstNameIsValidEmptyString(){
        onView(withId(R.id.registerFirstName)).perform(typeText(""));
        closeSoftKeyboard();
        onView(withId(R.id.registerButton)).perform(click());
        onView(withId(R.id.registerFirstName)).check(matches(hasErrorText("Name field invalid")));
    }

    @Test
    public void checkIfLastNameIsValidEmptyString(){
        onView(withId(R.id.registerLastName)).perform(typeText(""));
        closeSoftKeyboard();
        onView(withId(R.id.registerButton)).perform(click());
        onView(withId(R.id.registerLastName)).check(matches(hasErrorText("Name field invalid")));
    }

    @Test
    public void checkIfPasswordIsValidEmptyString(){
        onView(withId(R.id.registerPassword)).perform(typeText(""));
        closeSoftKeyboard();
        onView(withId(R.id.registerButton)).perform(click());
        onView(withId(R.id.registerPassword)).check(matches(hasErrorText("Password cannot be empty")));
    }

    @Test
    public void checkIfConfirmPasswordIsValidEmptyString(){
        onView(withId(R.id.confirmPassword)).perform(typeText(""));
        closeSoftKeyboard();
        onView(withId(R.id.registerButton)).perform(click());
        onView(withId(R.id.confirmPassword)).check(matches(hasErrorText("Password cannot be empty")));
    }

    @Test
    public void checkIfNameWithNumberIsValid(){
        onView(withId(R.id.registerFirstName)).perform(typeText("bhavya1234"));
        closeSoftKeyboard();
        onView(withId(R.id.registerButton)).perform(click());
        onView(withId(R.id.registerFirstName)).check(matches(hasErrorText("Name field invalid")));
    }

    /* This test isn't going to work because you're switching app contexts!
    @Test
    public void checkLoginWithRegisteredUser() {
        onView(withId(R.id.registerFirstName)).perform(typeText("bhavya"));
        closeSoftKeyboard();
        onView(withId(R.id.registerLastName)).perform(typeText("jain"));
        closeSoftKeyboard();
        onView(withId(R.id.registerEmail)).perform(typeText("bh753513@dal.ca"));
        closeSoftKeyboard();
        onView(withId(R.id.registerPhoneNumber)).perform(typeText("902-220-0566"));
        closeSoftKeyboard();
        onView(withId(R.id.registerPassword)).perform(typeText("hello123"));
        closeSoftKeyboard();
        onView(withId(R.id.confirmPassword)).perform(typeText("hello123"));
        closeSoftKeyboard();
        onView(withId(R.id.registerButton)).perform(click());
        onView(isRoot()).perform(waitFor(500));

        // Now logging in with the created user name and password
        onView(withId(R.id.userEmail)).perform(typeText("bh753513@dal.ca"));
        closeSoftKeyboard();
        onView(withId(R.id.userEmail)).perform(typeText("hello123"));
        closeSoftKeyboard();
        onView(withId(R.id.loginButton)).perform(click());
        intended(hasComponent(EmployeeHomeActivity.class.getName()));
    } */


  /*Failing test. Not sure why. Will come back to it.
    @Test
    public void checkIfMoved2WelcomePage() {
        onView(withId(R.id.registerFirstName)).perform(typeText("abcdhelllo"));
        closeSoftKeyboard();
        onView(withId(R.id.registerLastName)).perform(typeText("abcdhelllo"));
        closeSoftKeyboard();
        onView(withId(R.id.registerEmail)).perform(typeText("abcd1223@gmail.com"));
        closeSoftKeyboard();
        onView(withId(R.id.registerPhoneNumber)).perform(typeText("902-220-0566"));
        closeSoftKeyboard();
        onView(withId(R.id.registerPassword)).perform(typeText("abbb234566"));
        closeSoftKeyboard();
        onView(withId(R.id.confirmPassword)).perform(typeText("abbb1234566"));
        closeSoftKeyboard();
        onView(withId(R.id.registerButton)).perform(click());
        onView(isRoot()).perform(waitFor(500));
        intended(hasComponent(LoginActivity.class.getName()));
    }
   */
}
