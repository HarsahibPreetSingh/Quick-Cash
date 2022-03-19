package ca.dal.csci3130.quickcash.usermanagement;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static ca.dal.csci3130.quickcash.usermanagement.Delay.waitFor;

import android.content.Intent;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.hamcrest.Matcher;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.home.EmployerHomeActivity;
import ca.dal.csci3130.quickcash.home.PostJobsActivity;

public class PostJobsActivityEspressoTest {


    @Rule
    public ActivityScenarioRule<PostJobsActivity> myRule = new ActivityScenarioRule<>(PostJobsActivity.class);
    public IntentsTestRule<PostJobsActivity> myIntentRule = new IntentsTestRule<>(PostJobsActivity.class);


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
/* This test isn't going to work because you're switching app contexts!

    @Test
    public void checkIfMoved2EmployerHomepage() {
        onView(withId(R.id.jobLocationET)).perform(typeText("12,34"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.jobWageET)).perform(typeText("13"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.jobDurationET)).perform(typeText("5"));
        Espresso.closeSoftKeyboard();
        Espresso.onView(ViewMatchers.withId(R.id.constraintView)).perform(ViewActions.swipeUp());
        onView(withId(R.id.jobDescriptionET)).perform(typeText("I need someone" +
                " to repair my computer urgently cuz I have my project due soon!"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.postJobButton)).perform(click());
        onView(isRoot()).perform(waitFor(500));
        intended(hasComponent(EmployerHomeActivity.class.getName()));
    }
*/
}
