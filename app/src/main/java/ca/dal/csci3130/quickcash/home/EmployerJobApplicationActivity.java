package ca.dal.csci3130.quickcash.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.usermanagement.LoginActivity;
import ca.dal.csci3130.quickcash.usermanagement.SessionManager;

public class EmployerJobApplicationActivity extends AppCompatActivity {
    protected Intent currentActivity;
    SessionManager session;
    protected Job job;
    TextView jobTitle;
    TextView jobLocation;
    TextView jobWage;
    TextView jobDuration;
    TextView jobUrgency;
    TextView jobDescription;
    TextView employerName;

    // Useful functions for redirects
    public Intent getCurrentActivity() { return currentActivity ; }
    public void setCurrentActivity (Intent currentActivity) {this.currentActivity = currentActivity;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_application);
        Bundle extras = getIntent().getExtras();
        System.out.println("Employer Reached outside");

        tailorActivityUIForEmployer();

        Button applicantListButton = findViewById(R.id.applicantsListButton);
        applicantListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), JobApplicantsActivity.class));
            }

        });

        jobTitle = findViewById(R.id.jobTitle);
        setJobTitle(extras);

        // Get all the extras
        // Then set the texts of TextViews like above using a method
        // -----------------------------------------------------------------------------------------
    }

    public void setJobTitle(@NonNull Bundle extras) {
        if (extras.getString("JobTitle") != null) {
            jobTitle.setText(extras.getString("JobTitle"));
        }
    }

    public void tailorActivityUIForEmployer() {
        TextView employeeAppliedStatus = findViewById(R.id.jobAppliedStatus);
        employeeAppliedStatus.setVisibility(View.INVISIBLE);

        Button applyButton = findViewById(R.id.jobApplyButton);
        applyButton.setVisibility(View.INVISIBLE);
    }
}
