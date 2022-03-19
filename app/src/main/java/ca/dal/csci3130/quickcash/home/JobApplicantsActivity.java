package ca.dal.csci3130.quickcash.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.usermanagement.SessionManager;

public class JobApplicantsActivity extends AppCompatActivity {
    protected RecyclerView recyclerView;
    protected Intent currentActivity;
    public SessionManager session;
    protected Job currentJob;


    // Useful functions for redirects
    public Intent getCurrentActivity() { return currentActivity ; }
    public void setCurrentActivity (Intent currentActivity) {this.currentActivity = currentActivity;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_applicants);

        session = new SessionManager(getApplicationContext());

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        // The following code is for testing applicant cards only
        // Needs to be replaced with Firebase implementation
        // ApplicantRecyclerAdapter needs to be completed after that to respond to accept button
        //---------------------------------------------------------------------------------------------
        ArrayList<String[]> applicants = new ArrayList<String[]>();
        String[] applicant1 = {"1","2","3"};
        applicants.add(applicant1);
        applicants.add(applicant1);
        displayJobsPosted(applicants);
        //---------------------------------------------------------------------------------------------

    }

    private void displayJobsPosted(ArrayList<String[]> applicants) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        ApplicantRecyclerAdapter adapter = new ApplicantRecyclerAdapter(applicants);
        recyclerView.setAdapter(adapter);
    }
}
