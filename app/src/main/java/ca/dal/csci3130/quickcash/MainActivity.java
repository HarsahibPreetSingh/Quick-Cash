package ca.dal.csci3130.quickcash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import ca.dal.csci3130.quickcash.home.EmployeeHomeActivity;
import ca.dal.csci3130.quickcash.home.EmployerHomeActivity;
import ca.dal.csci3130.quickcash.home.JobsPostedActivity;
import ca.dal.csci3130.quickcash.usermanagement.LoginActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Login Page
        Intent loginActivity = new Intent(this, LoginActivity.class);


        startActivity(loginActivity);
    }
}