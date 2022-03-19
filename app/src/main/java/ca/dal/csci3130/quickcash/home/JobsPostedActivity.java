package ca.dal.csci3130.quickcash.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

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

public class JobsPostedActivity extends AppCompatActivity {
    protected RecyclerView recyclerView;
    protected Intent currentActivity;
    public SessionManager session;
    protected Job currentJob;

    protected DatabaseReference DBJobRef = FirebaseDatabase.getInstance().getReference("Jobs");


    // Useful functions for redirects
    public Intent getCurrentActivity() { return currentActivity ; }
    public void setCurrentActivity (Intent currentActivity) {this.currentActivity = currentActivity;}

    /**
     * Initialize a recyclerView to display job cards. Set event listener on "Jobs" collection
     * in database, and update displayed jobs in Recyclerview when data changes in the DB.
     * Job cards are only shown to the user if they have created that job.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs_posted);

        session = new SessionManager(getApplicationContext());

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        // Set listener for jobs. Filter them out based on if the current user's Hash matches
        // the "author hash" of all available jobs.
        ValueEventListener jobValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot jobDataSnapshot) {
                // If the view is updated, we want to completely reload jobs
                ArrayList<Job> jobs = filterJobsBasedOnUserHash(jobDataSnapshot);
                displayJobsPosted(jobs);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };

        DBJobRef.addValueEventListener(jobValueEventListener);
    }

    /**
     * @param jobDataSnapshot : The "Job" collection from the database
     * @return The arrayList of Jobs where the job poster user hash matches the local user hash
     */
    private ArrayList<Job> filterJobsBasedOnUserHash(DataSnapshot jobDataSnapshot) {
        // If the view is updated, we want to completely reload jobs
        ArrayList<Job> myPostedJobs = new ArrayList<Job>();

        for (DataSnapshot ds : jobDataSnapshot.getChildren()) {
            currentJob = ds.getValue(Job.class);
            currentJob.setJobHash(ds.getKey());
            System.out.println("Key: "+ds.getKey()+" Value: "+ds.getValue());

            if (currentJob.getJobOwnerHash() != null) {
                if (currentJob.getJobOwnerHash().equals(session.getUserHash())) {
                    myPostedJobs.add(currentJob);
                }
            }
        }

        return myPostedJobs;
    }

    /**
     * Recyclerview logic to display specific job cards.
     * @param jobs : The jobs which the current user has created.
     */
    private void displayJobsPosted(ArrayList<Job> jobs) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        JobRecyclerAdapter adapter = new JobRecyclerAdapter(jobs);
        recyclerView.setAdapter(adapter);
    }
}
