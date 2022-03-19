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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.usermanagement.User;
import ca.dal.csci3130.quickcash.usermanagement.UserInterface;
import ca.dal.csci3130.quickcash.usermanagement.JobInterface;
import ca.dal.csci3130.quickcash.usermanagement.SessionManager;

public class JobBoardActivity extends AppCompatActivity {
    protected RecyclerView recyclerView;
    protected Intent currentActivity;
    public SessionManager session;
    protected Job currentJob;

    private ArrayList<String> fetchTitleList;
    private String fetchDuration;
    private String fetchLocation;
    private String fetchWage;

    protected DatabaseReference DBJobRef = FirebaseDatabase.getInstance().getReference("Jobs");

    public Intent getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(Intent currentActivity) {
        this.currentActivity = currentActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_board);

        initializeVariables();
        retrieveUserPreferencesFromFirebase();
        session = new SessionManager(getApplicationContext());

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        // Set listener for jobs. Filter them out based on if the current user's Hash matches
        // the "author hash" of all available jobs.
        ValueEventListener jobValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot jobDataSnapshot) {
//                 If the view is updated, we want to completely reload jobs
                ArrayList<Job> jobs = filterJobsBasedOnUserPreferences(jobDataSnapshot);
                if(!jobs.isEmpty()) {
                    displayJobsPosted(jobs);
                }
                else{
                    jobs = getAllJobs(jobDataSnapshot);
                    displayJobsPosted(jobs);
                }
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
    private ArrayList<Job> getAllJobs(DataSnapshot jobDataSnapshot) {
        // If the view is updated, we want to completely reload jobs
        ArrayList<Job> allJobsPosted = new ArrayList<Job>();

        for (DataSnapshot ds : jobDataSnapshot.getChildren()) {
            currentJob = ds.getValue(Job.class);
            currentJob.setJobHash(ds.getKey());
            allJobsPosted.add(currentJob);
        }

        return allJobsPosted;
    }

    public void initializeVariables() {
        fetchTitleList = new ArrayList<>();

    }


    /**
     * Recyclerview logic to display specific job cards.
     *
     * @param jobs : The jobs which the current user has created.
     */
    private void displayJobsPosted(ArrayList<Job> jobs) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        JobRecyclerAdapter adapter = new JobRecyclerAdapter(jobs);
        recyclerView.setAdapter(adapter);
    }


    private void retrieveUserPreferencesFromFirebase() {
        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance()
                .getCurrentUser().getUid()).child("jobPreferences").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("here");
                if (dataSnapshot.exists()) {
                    System.out.println("here1");
                    Map<String, Object> jobPrefMap = (Map<String, Object>) dataSnapshot.getValue();
                    fetchTitleList.addAll((ArrayList<String>) jobPrefMap.get("jobWithTitle"));
                    fetchDuration = ((String) jobPrefMap.get("jobMaxDuration"));
                    fetchLocation = ((String) jobPrefMap.get("jobMaxLocation"));
                    fetchWage = ((String) jobPrefMap.get("jobMinWage"));

                    System.out.println("title" +fetchTitleList);
                    System.out.println("du" + fetchDuration);
                    System.out.println("Lo" + fetchLocation);
                    System.out.println("Wage" + fetchWage);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    /**
     * @param jobDataSnapshot : The "Job" collection from the database
     * @return The arrayList of Jobs where the job poster user hash matches the local user hash
     */
    private ArrayList<Job> filterJobsBasedOnUserPreferences(DataSnapshot jobDataSnapshot) {

        ArrayList<Job> preferredJobs = new ArrayList<Job>();
        for (DataSnapshot ds : jobDataSnapshot.getChildren()) {
            boolean added = false;
            currentJob = ds.getValue(Job.class);
            currentJob.setJobHash(ds.getKey());


            if(!fetchTitleList.isEmpty()) {
                for (String val : fetchTitleList) {
                    if (val != null && !added) {
                        if (currentJob.getJobTitle().equals(val)) {
                            added = true;
                            preferredJobs.add(currentJob);
                        }
                    }
                }
            }

            if (fetchDuration != null) {
                if ((!fetchDuration.isEmpty()) && !added) {
                    if (Integer.parseInt(currentJob.getJobDuration()) <= Integer.parseInt(fetchDuration)) {
                        added = true;
                        preferredJobs.add(currentJob);
                    }
                }
            }


            /*
             * We don't longitude and lattitude in this query, so we can't use this function for now.
             * */
//                if (fetchLocation != null && !added) {
//                    if (currentJob.getJobLocation().compareTo(fetchLocation) <= 0) {
//                        System.out.println("3:" + currentJob.getJobTitle() + " " + currentJob.getJobLocation() + " " + fetchLocation + " " + currentJob.getJobLocation().compareTo(fetchLocation));
//                        added = true;
//                        preferredJobs.add(currentJob);
//                    }
//                }


            if (fetchWage != null) {
                if ((!fetchWage.isEmpty()) && !added) {
                    if (Integer.parseInt(currentJob.getJobWage()) >= Integer.parseInt(fetchWage)) {
                        preferredJobs.add(currentJob);
                    }
                }
            }
        }

        return preferredJobs;
    }


}

