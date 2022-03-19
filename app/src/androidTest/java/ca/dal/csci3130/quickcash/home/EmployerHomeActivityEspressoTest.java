package ca.dal.csci3130.quickcash.home;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.usermanagement.LoginActivity;

public class EmployerHomeActivityEspressoTest {

    @Rule
    public ActivityScenarioRule<EmployerHomeActivity> myRule = new ActivityScenarioRule<>(EmployerHomeActivity.class);
    public IntentsTestRule<EmployerHomeActivity> myIntentRule = new IntentsTestRule<>(EmployerHomeActivity.class);

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
        assertEquals("ca.dal.csci3130.quickcash", appContext.getPackageName());
    }

    // BELOW THREE TESTS ARE COMMENTED OUT BECAUSE THEY TEST REDIRECTS AND BREAK TESTING IN THE
    // PIPELINE. WE HAVE DECIDED TO JUST MANUALLY TEST REDIRECTS WHEN WE UPDATE DEVELOPMENT CODE
    // BECAUSE WE KIND OF HAVE TO ANYWAY

    /*
    @Test
    public void checkIfMoved2JobsPostedPage() {
        onView(withId(R.id.jobsPostedButton)).perform(click());
        intended(hasComponent(JobsPostedActivity.class.getName()));
    }

    @Test
    public void checkIfMoved2PostJobsPage() {
        onView(withId(R.id.postJobsButton)).perform(click());
        intended(hasComponent(PostJobsActivity.class.getName()));
    }

    @Test
    public void checkIfMoved2LoginPage() {
        onView(withId(R.id.logOutButton)).perform(click());
        intended(hasComponent(LoginActivity.class.getName()));
    }

     */

}
