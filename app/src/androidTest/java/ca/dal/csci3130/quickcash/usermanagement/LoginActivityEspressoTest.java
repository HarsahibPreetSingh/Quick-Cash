package ca.dal.csci3130.quickcash.usermanagement;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ca.dal.csci3130.quickcash.R;

public class LoginActivityEspressoTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> myRule = new ActivityScenarioRule<>(LoginActivity.class);
    public IntentsTestRule<LoginActivity> myIntentRule = new IntentsTestRule<>(LoginActivity.class);

    @Before
    public void setup() {
        Intents.init();
    }

    @AfterClass
    public static void tearDown() {
        System.gc();
    }

    @Test
    public void avoidAnnoyingErrorMessageWhenRunningTestsInAnt() {
        assertEquals("true", "true"); // do nothing;
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("ca.dal.cs.csci3130.quickcash.usermanagement", appContext.getPackageName());
    }

    // BELOW THREE TESTS ARE COMMENTED OUT BECAUSE THEY TEST REDIRECTS AND BREAK TESTING IN THE
    // PIPELINE. WE HAVE DECIDED TO JUST MANUALLY TEST REDIRECTS WHEN WE UPDATE DEVELOPMENT CODE
    // BECAUSE WE KIND OF HAVE TO ANYWAY

    /*
    @Test
    public void checkIfMoved2SignupPage() {
        onView(withId(R.id.createAccountButton)).perform(click());
        intended(hasComponent(SignupActivity.class.getName()));
    }
    */

    /*
    @Test
    public void checkIfMoved2EmployeeHomePage() {
        onView(withId(R.id.userEmail)).perform(typeText("samabarbanel3@gmail.com"));
        onView(withId(R.id.userPassword)).perform(typeText("samabarbanel1"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.loginButton)).perform(click());
        waitFor(500);
        intended(hasComponent(EmployeeHomeActivity.class.getName()));
    }
     */

    /*
    @Test
    public void checkIfMoved2EmployerHomePage() {
        onView(withId(R.id.userEmail)).perform(typeText("abc123@dal.ca"));
        onView(withId(R.id.userPassword)).perform(typeText("Xy!12345"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.loginButton)).perform(click());

        intended(hasComponent(EmployeeHomeActivity.class.getName()));
    }
     */

    @Test
    public void checkIfLoginDoesNotValidates() {
        onView(withId(R.id.userEmail)).perform(typeText("abc123dal.ca"));
        onView(withId(R.id.userPassword)).perform(typeText("!45"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.loginButton)).perform(click());

        Espresso.onView(withId(R.id.loginErrorMessage)).check(matches(withText("Wrong Credentials! Please enter again")));
    }
}
