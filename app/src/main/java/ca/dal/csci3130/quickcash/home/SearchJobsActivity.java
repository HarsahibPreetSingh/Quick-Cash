package ca.dal.csci3130.quickcash.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class SearchJobsActivity extends AppCompatActivity {
    protected RecyclerView recyclerView;
    protected Intent currentActivity;
    public SessionManager session;
    protected Job currentJob;
    private Button btn;
    protected DataSnapshot searchSnapshot;
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
        setContentView(R.layout.activity_search_jobs);

        session = new SessionManager(getApplicationContext());
        btn = findViewById(R.id.search_button);
        recyclerView = findViewById(R.id.recyclerView2);
        recyclerView.setHasFixedSize(true);

        // Set listener for jobs. Filter them out based on if the current user's Hash matches
        // the "author hash" of all available jobs.
        ValueEventListener jobValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot jobDataSnapshot) {
                // If the view is updated, we want to completely reload jobs
                searchSnapshot = jobDataSnapshot;
                ArrayList<Job> jobs = filterJobsBasedOnJobTitle(jobDataSnapshot);
                displayJobsPosted(jobs);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };

        DBJobRef.addValueEventListener(jobValueEventListener);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Job> jobs = filterJobsBasedOnJobTitle(searchSnapshot);
                displayJobsPosted(jobs);
            }
        });
    }

    /**
     * @param jobDataSnapshot : The "Job" collection from the database
     * @return The arrayList of Jobs where the job poster user hash matches the local user hash
     */
    private ArrayList<Job> filterJobsBasedOnJobTitle(DataSnapshot jobDataSnapshot) {
        // If the view is updated, we want to completely reload jobs
        ArrayList<Job> myPostedJobs = new ArrayList<Job>();

        EditText mEdit   = findViewById(R.id.searchJobInput);
        String str  = mEdit.getText().toString();
        /*
        Checking if the user has entered something in the search area and if now then print all the
        available jobs.
         */
        if (str.trim().length() > 0){
            for (DataSnapshot ds : jobDataSnapshot.getChildren()) {
                currentJob = ds.getValue(Job.class);
                currentJob.setJobHash(ds.getKey());

                /*
                here we are checking if the title is having those words contained then print the
                jobs whose titles match the keywords
                 */
                if (currentJob.getJobOwnerHash() != null) {
                    String job_title = currentJob.getJobTitle().toLowerCase();
                    str = str.toLowerCase();
                    if(job_title.contains(str)){
                        myPostedJobs.add(currentJob);
                    }
                }

            }
        }
        else{
            for (DataSnapshot ds : jobDataSnapshot.getChildren()) {
                currentJob = ds.getValue(Job.class);
                currentJob.setJobHash(ds.getKey());
                if (currentJob.getJobOwnerHash() != null) {
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
