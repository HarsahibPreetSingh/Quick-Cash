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

public class PostJobsActivity extends AppCompatActivity{

    Job job;
    SessionManager session;
    protected Intent currentActivity;

    private EditText jobTitleET, jobLocationET, jobWageET, jobDurationET, jobDescriptionET;

    private String jobTitle, jobLocation, jobUrgency, jobDescription,
            employeePicked, jobWage, jobDuration;

    private Button postJobsButton;

    Spinner jobTitleSpinner;
    Spinner jobUrgencySpinner;

    // FIREBASE
    private DatabaseReference getFirebaseDBRef;
    private FirebaseDatabase firebaseDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_jobs);

        initializeVariables();
        initializeDB();
        job = new Job();

        postJobsButton = findViewById(R.id.postJobButton);
        postJobsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jobLocation = jobLocationET.getText().toString();
                jobDescription = jobDescriptionET.getText().toString();
                jobWage = jobWageET.getText().toString();
                jobDuration = jobDurationET.getText().toString();

                job.setJobLocation(jobLocation);
                job.setJobDescription(jobDescription);
                job.setJobWage(jobWage);
                job.setJobDuration(jobDuration);
                job.setEmployerName(session.getKeyEmail());
                job.setJobOwnerHash(session.getUserHash());
                job.setEmployeePicked(null);

                // We need to add an empty item or job applications is not added
                job.setJobApplications(new ArrayList<String>());
                job.addJobApplication("");


                addJobToDatabase(job);
            }
        });

        setSpinner(jobTitleSpinner, R.array.jobs_array);
        setSpinner(jobUrgencySpinner, R.array.urgency_array);

    }

    public void initializeVariables(){
        session = new SessionManager(getApplicationContext());
        jobLocationET = findViewById(R.id.jobLocationET);
        jobDescriptionET = findViewById(R.id.jobDescriptionET);
        jobWageET = findViewById(R.id.jobWageET);
        jobDurationET = findViewById(R.id.jobDurationET);
        jobTitleSpinner = (Spinner) findViewById(R.id.jobTitle);
        jobUrgencySpinner = (Spinner) findViewById(R.id.jobUrgency);

    }

    public void initializeDB() {
        //mAuth = FirebaseAuth.getInstance();
        firebaseDB = FirebaseDatabase.getInstance(Constants.FIREBASE_URL);
        getFirebaseDBRef = firebaseDB.getReference("Jobs");
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

                if (spinner.getId() == R.id.jobTitle) {
                    job.setJobTitle((String) parent.getItemAtPosition(position));
                }
                else {
                    job.setJobUrgency((String) parent.getItemAtPosition(position));
                }
                Log.v("item", (String) parent.getItemAtPosition(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

    }

    protected void addJob(JobInterface jobInterface) {
        JobDAO jobDAO = new JobDAO();
        jobDAO.add(jobInterface);
    }

    protected Task<Void> addJobToDatabase(Job job) {
        Map<String, Object> map = new HashMap<>();
        map.put("jobTitle", job.getJobTitle().toString());
        map.put("jobLocation", job.getJobLocation().toString());
        map.put("jobWage", job.getJobWage().toString());
        map.put("jobDuration", job.getJobDuration().toString());
        map.put("jobUrgency", job.getJobUrgency().toString());
        map.put("jobDescription", job.getJobDescription().toString());
        map.put("employeePicked", "none");
        map.put("employerName", job.getEmployerName().toString());
        map.put("jobOwnerHash", session.getUserHash());
        map.put("jobApplications", job.getJobApplications());

        firebaseDB.getReference()
               .child(Constants.JOBS_COLLECTION)
               .push()
               .setValue(map)
               .addOnSuccessListener(aVoid -> {
                   Toast.makeText(getApplicationContext(), "Job added successfully", Toast.LENGTH_SHORT).show();
                   Intent i = new Intent(getApplicationContext(), EmployerHomeActivity.class);
                   startActivity(i);
                   setCurrentActivity(i);
               })
               .addOnFailureListener(e ->
                       Toast.makeText(getApplicationContext(), "Job insertion failed", Toast.LENGTH_SHORT).show());

            return null;
        }

    public Intent getCurrentActivity() { return currentActivity ; }
    public void setCurrentActivity (Intent currentActivity) {this.currentActivity = currentActivity;}


}
