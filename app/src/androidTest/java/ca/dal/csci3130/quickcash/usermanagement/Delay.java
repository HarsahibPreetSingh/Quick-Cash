
// https://stackoverflow.com/questions/21417954/espresso-thread-sleep
// This link above is where I learned how write this class and wrote the code for it.
package ca.dal.csci3130.quickcash.usermanagement;

import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import android.view.View;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;

import org.hamcrest.Matcher;

public class Delay {
    public static ViewAction waitFor(final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "Wait for " + millis + " milliseconds.";
            }

            @Override
            public void perform(UiController uiController, final View view) {
                uiController.loopMainThreadForAtLeast(millis);
            }
        };
    }
}