package ca.dal.csci3130.quickcash.home;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.usermanagement.LoginActivity;
import ca.dal.csci3130.quickcash.usermanagement.SessionManager;

public class EmployeeHomeActivity extends AppCompatActivity
        implements View.OnClickListener {

    protected Intent currentActivity;
    SessionManager session;
    TextView welcomeEmployeeNameTV;
    Button jobBoardButton;
    Button searchJobsButton;
    Button jobPreferencesButton;
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
        setContentView(R.layout.activity_employee_home);

        welcomeEmployeeNameTV = findViewById(R.id.welcomeEmployeeNameTV);
        welcomeEmployeeNameTV.setText("Welcome " + session.getKeyIsEmployee() + " " + session.getKeyName());

        setButtons();


    }

    /**
     * Initializes buttons and sets listeners for them
     */
    public void setButtons(){

        jobBoardButton = findViewById(R.id.jobBoardButton);
        jobBoardButton.setOnClickListener(this);

        searchJobsButton = findViewById(R.id.searchJobsButton);
        searchJobsButton.setOnClickListener(this);

        jobPreferencesButton = findViewById(R.id.jobPreferencesButton);
        jobPreferencesButton.setOnClickListener(this);

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

        if (view.getId() == R.id.jobBoardButton) {
            intent = new Intent(this, JobBoardActivity.class);
        }
        else if (view.getId() == R.id.searchJobsButton) {
            intent = new Intent(this, SearchJobsActivity.class);
        }
        else if (view.getId() == R.id.jobPreferencesButton) {
            intent = new Intent(this, JobPreferencesActivity.class);
        }
        else if (view.getId() == R.id.logOutButton) {
            session.logoutUser();
            intent = new Intent(this, LoginActivity.class);
        }
        else {intent = null;}

        startActivity(intent);
        setCurrentActivity(intent);

    }



}