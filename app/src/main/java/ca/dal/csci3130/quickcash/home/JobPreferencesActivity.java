package ca.dal.csci3130.quickcash.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.common.Constants;
import ca.dal.csci3130.quickcash.usermanagement.JobDAO;
import ca.dal.csci3130.quickcash.usermanagement.JobInterface;
import ca.dal.csci3130.quickcash.usermanagement.LoginActivity;
import ca.dal.csci3130.quickcash.usermanagement.SessionManager;
import ca.dal.csci3130.quickcash.usermanagement.User;

public class JobPreferencesActivity extends AppCompatActivity {

    Job job1, job2, job3;
    ArrayList<Job> jobArrayList;
    ArrayList<String> selectedJobTypes;
    SessionManager session;
    protected Intent currentActivity;
    HashMap<String, Object> jobPrefMap;

    private EditText jobType1ET, jobType2ET, jobType3ET,
            jobMaxLocationDistanceET, jobMinWageET, jobMaxDurationET;

    private String jobType1, jobType2, jobType3, jobMaxLocationDistance, jobMinWage, jobMaxDuration;

    private Button updatePrefsButton;

    Spinner jobTypeSpinner1 ,jobTypeSpinner2, jobTypeSpinner3;

    // FIREBASE
    private DatabaseReference getFirebaseDBRef;
    private FirebaseDatabase firebaseDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_preferences);

        initializeVariables();
        job1 = new Job();
        job2 = new Job();
        job3 = new Job();
        jobPrefMap = new HashMap<>();
        jobArrayList = new ArrayList<>();
        selectedJobTypes = new ArrayList<>();

        updatePrefsButton = findViewById(R.id.updatePrefsButton);
        updatePrefsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jobMaxLocationDistance = jobMaxLocationDistanceET.getText().toString();
                jobMinWage = jobMinWageET.getText().toString();
                jobMaxDuration = jobMaxDurationET.getText().toString();

                setJobPreferredAttributes(job1, jobMaxLocationDistance, jobMinWage, jobMaxDuration);
                setJobPreferredAttributes(job2, jobMaxLocationDistance, jobMinWage, jobMaxDuration);
                setJobPreferredAttributes(job3, jobMaxLocationDistance, jobMinWage, jobMaxDuration);
                setJobPreferenceMapAttributes(jobPrefMap);

                addUpdateJobPrefs(jobPrefMap);
            }
        });

        setSpinner(jobTypeSpinner1, R.array.jobs_array);
        setSpinner(jobTypeSpinner2, R.array.jobs_array);
        setSpinner(jobTypeSpinner3, R.array.jobs_array);
    }

    public void initializeVariables(){
        session = new SessionManager(getApplicationContext());
        jobMaxLocationDistanceET = findViewById(R.id.jobMaxLocationDistanceET);
        jobMinWageET = findViewById(R.id.jobMinWageET);
        jobMaxDurationET = findViewById(R.id.jobMaxDurationET);
        jobTypeSpinner1 = (Spinner) findViewById(R.id.jobType1);
        jobTypeSpinner2 = (Spinner) findViewById(R.id.jobType2);
        jobTypeSpinner3 = (Spinner) findViewById(R.id.jobType3);
    }

    public void setJobPreferenceMapAttributes(HashMap<String, Object> map){
        for(Job job : jobArrayList){
            if(!job.getJobTitle().equalsIgnoreCase("N/A") && !selectedJobTypes.contains(job.getJobTitle())){
                selectedJobTypes.add(job.getJobTitle());
            }
        }
        map.put("jobWithTitle",selectedJobTypes);
        map.put("jobMaxDuration",jobMaxDuration);
        map.put("jobMinWage", jobMinWage);
        map.put("jobMaxLocation",jobMaxLocationDistance);
        System.out.println(map);
    }

    public void setJobPreferredAttributes(Job job, String jobMaxLocationDistance,
                                         String jobMinWage, String jobMaxDuration) {
        job.setJobLocation(jobMaxLocationDistance);
        job.setJobWage(jobMinWage);
        job.setJobDuration(jobMaxDuration);
        jobArrayList.add(job);
    }

    public void setSpinner(Spinner spinner, int arrayItem){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                arrayItem, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                if (spinner.getId() == R.id.jobType1) {
                    job1.setJobTitle((String) parent.getItemAtPosition(position));
                }
                if (spinner.getId() == R.id.jobType2) {
                    job2.setJobTitle((String) parent.getItemAtPosition(position));
                }
                if (spinner.getId() == R.id.jobType3) {
                    job3.setJobTitle((String) parent.getItemAtPosition(position));
                }

                Log.v("item", (String) parent.getItemAtPosition(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

    }

    public void addUpdateJobPrefs(HashMap<String, Object> map){
        String hash = session.getUserHash();
        System.out.println(hash);
        firebaseDB = FirebaseDatabase.getInstance(Constants.FIREBASE_URL);
        getFirebaseDBRef = firebaseDB.getReference("Users");

        getFirebaseDBRef.child(hash).child("jobPreferences").setValue(map).addOnSuccessListener(aVoid -> {
            Toast.makeText(getApplicationContext(), "Job Preference added successfully", Toast.LENGTH_SHORT).show();
            jobArrayList.clear();
            selectedJobTypes.clear();
            jobPrefMap.clear();
            Intent i = new Intent(getApplicationContext(), EmployeeHomeActivity.class);
            startActivity(i);
            setCurrentActivity(i);
        }).addOnFailureListener(e ->
                Toast.makeText(getApplicationContext(), "Job Preference insertion failed. Enter Again", Toast.LENGTH_SHORT).show());

    }


    public Intent getCurrentActivity() { return currentActivity ; }
    public void setCurrentActivity (Intent currentActivity) {this.currentActivity = currentActivity;}
}
