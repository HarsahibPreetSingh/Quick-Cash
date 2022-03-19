package ca.dal.csci3130.quickcash.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.usermanagement.LoginActivity;
import ca.dal.csci3130.quickcash.usermanagement.SessionManager;

public class EmployerHomeActivity extends AppCompatActivity
        implements View.OnClickListener  {

    protected Intent currentActivity;
    SessionManager session;
    TextView welcomeEmployerNameTV;
    Button jobsPostedButton;
    Button postJobsButton;
    Button logOutButton;

    // Useful functions for redirects
    public Intent getCurrentActivity() { return currentActivity ; }
    public void setCurrentActivity (Intent currentActivity) {this.currentActivity = currentActivity;}


    /**
     * Collects all requires user information for the current activity.
     * Initializes interactive UI components.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionManager(getApplicationContext());
        setContentView(R.layout.activity_employer_home);

        welcomeEmployerNameTV = findViewById(R.id.welcomeEmployerNameTV);
        welcomeEmployerNameTV.setText("Welcome " + session.getKeyIsEmployee() + " " + session.getKeyName());

        setButtons();

    }


    /**
     * Initializes buttons and sets listeners for them
     */
    public void setButtons(){
        jobsPostedButton = findViewById(R.id.jobsPostedButton);
        jobsPostedButton.setOnClickListener(this);

        postJobsButton = findViewById(R.id.postJobsButton);
        postJobsButton.setOnClickListener(this);

        logOutButton = findViewById(R.id.logOutButton);
        logOutButton.setOnClickListener(this);
    }

    // Disabling the back button on the home page to prevent going back to login page
    @Override
    public void onBackPressed() {
        // Do nothing (don't go back to login page)
    }

    @Override
    public void onClick(View view) {
        Intent intent;

        if (view.getId() == R.id.jobsPostedButton) {
            intent = new Intent(this, JobsPostedActivity.class);
        }
        else if (view.getId() == R.id.postJobsButton) {
            intent = new Intent(this, PostJobsActivity.class);
        }
        else if (view.getId() == R.id.logOutButton){
            session.logoutUser();
            intent = new Intent(this, LoginActivity.class);
        }
        else {intent = null;}


        startActivity(intent);
        setCurrentActivity(intent);
    }
}