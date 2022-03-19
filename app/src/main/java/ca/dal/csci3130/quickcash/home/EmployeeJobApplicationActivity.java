package ca.dal.csci3130.quickcash.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.common.Constants;
import ca.dal.csci3130.quickcash.usermanagement.SessionManager;

public class EmployeeJobApplicationActivity extends AppCompatActivity {
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
    private String employeeHash;
    private boolean applied;
    long countApplications;

    Bundle extras;

    private FirebaseDatabase firebaseDB;
    private DatabaseReference getFirebaseDBRef;

    // Useful functions for redirects
    public Intent getCurrentActivity() { return currentActivity ; }
    public void setCurrentActivity (Intent currentActivity) {this.currentActivity = currentActivity;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        applied = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_application);

        System.out.println("Employee Reached outside");
        tailorActivityUIForEmployee();

        System.out.println("Reached inside");
        extras = getIntent().getExtras();

        System.out.println("Applied: "+applied);
        checkEmployeeHashInJobApplication(extras);
        System.out.println("Applied: "+applied);

        session = new SessionManager(getApplicationContext());

        if(applied){
            System.out.println("Already exists");
            changeUIForExistingUser();
        }

        Button applyButton = findViewById(R.id.jobApplyButton);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // The apply button functionality needs to be implemented
                //---------------------------------------------------------------------------------------------
                employeeHash = session.getUserHash();

                if(!applied){
                    addEmployeeHashToJobApplication(countApplications);
                }
                else{
                    System.out.println("Already exists");
                    changeUIForExistingUser();
                }
            }

        });

        jobTitle = findViewById(R.id.jobTitle);
        setJobTitle(extras);

        // Get all the extras
        // Then set the texts of TextViews like above using a method
        // -----------------------------------------------------------------------------------------
    }

    public void checkEmployeeHashInJobApplication(Bundle extras){
        System.out.println("Reached inside checkEmployeeHashInJobApplication");
        firebaseDB = FirebaseDatabase.getInstance(Constants.FIREBASE_URL);
        String jobHash = extras.getString("jobHash");
        getFirebaseDBRef = firebaseDB.getReference("Jobs").child(jobHash).child("jobApplications");


        System.out.println("Intent is "+jobHash);

        getFirebaseDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                countApplications = snapshot.getChildrenCount();
                System.out.println("Inside db Count: "+countApplications);

                for(DataSnapshot d : snapshot.getChildren()){
                    System.out.println("Key: "+d.getKey()+" Value: "+d.getValue());
                    if(d.getValue().equals(session.getUserHash())){
                        applied = true;
//                        addEmployeeHashToJobApplication(countVal);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        System.out.println("Outside db Count: "+countApplications);
    }

    public void addEmployeeHashToJobApplication(long presentJobApplications){
        System.out.println("Reached inside addEmployeeHashToJobApplication");
        long countAfterApplying = presentJobApplications;
        firebaseDB = FirebaseDatabase.getInstance(Constants.FIREBASE_URL);
        String jobHash = extras.getString("jobHash");
        getFirebaseDBRef = firebaseDB.getReference("Jobs").child(jobHash).child("jobApplications");

        getFirebaseDBRef.child(Long.toString(countAfterApplying)).setValue(employeeHash).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                //Code to change UI details
                changeUIForExistingUser();
//                TextView appliedStatus = findViewById(R.id.jobAppliedStatus);
//                appliedStatus.setText("Applied");
//                Button applyButton = findViewById(R.id.jobApplyButton);
//                applyButton.setText("Applied");
//                applyButton.setEnabled(false);
            }
        });
        countAfterApplying += 1;
    }

    public void changeUIForExistingUser(){
        TextView appliedStatus = findViewById(R.id.jobAppliedStatus);
        appliedStatus.setText("Applied");
        Button applyButton = findViewById(R.id.jobApplyButton);
        applyButton.setText("Applied");
        applyButton.setEnabled(false);
    }

    public void setJobTitle(@NonNull Bundle extras) {
        if (extras.getString("JobTitle") != null) {
            jobTitle.setText(extras.getString("JobTitle"));
        }
    }

    public void tailorActivityUIForEmployee() {
        TextView employerApplicantsStatus = findViewById(R.id.jobApplicantStatus);
        employerApplicantsStatus.setVisibility(View.INVISIBLE);

        Button applicantsListButton = findViewById(R.id.applicantsListButton);
        applicantsListButton.setVisibility(View.INVISIBLE);
    }
}
