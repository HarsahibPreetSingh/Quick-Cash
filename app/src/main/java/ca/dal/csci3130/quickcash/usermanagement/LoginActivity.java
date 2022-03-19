package ca.dal.csci3130.quickcash.usermanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.home.EmployeeHomeActivity;
import ca.dal.csci3130.quickcash.home.EmployerHomeActivity;

public class LoginActivity extends AppCompatActivity {
    protected Intent currentActivity;
    private FirebaseAuth mAuth;
    private String phone, isEmp, fullName, sessionEmail, sessionPass;
    private String validateUserEmail, validateUserPassword;

    public SessionManager session;

    // Useful functions for redirects
    public Intent getCurrentActivity() { return currentActivity ; }
    public void setCurrentActivity (Intent currentActivity) {this.currentActivity = currentActivity;}

    /**
     * Initializes a new session object which will hold the current user's details
     * Performs input validation through both Regex and the database.
     * Handles all onClicklisteners() and redirects.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        session = new SessionManager(getApplicationContext());

        Intent signupActivity = new Intent(this, SignupActivity.class);

        // Getting inputted information from login screen
        Button loginButton = findViewById(R.id.loginButton);
        TextView userEmail = findViewById(R.id.userEmail);
        TextView userPassword = findViewById(R.id.userPassword);

        loginButton.setOnClickListener(view -> {
            validateUserEmail = userEmail.getText().toString();
            validateUserPassword = userPassword.getText().toString();

            // boolean values to check if the Email and Password are properly typed or not
            boolean emailValid = validateEmail(validateUserEmail);
            boolean passValid = validatePass(validateUserPassword);

            if (emailValid && passValid) {
                loginUserWithFirebase(validateUserEmail, validateUserPassword);
            }
            else {
                setStatusMessage("Wrong Credentials! Please enter again");
            }
        });

        // Move user to signup page when they click "Register"
        Button createAccountButton = findViewById(R.id.createAccountButton);
        createAccountButton.setOnClickListener(view -> {
            startActivity(signupActivity);
            setCurrentActivity(signupActivity);
        });

        // Forgot Password -- NOT IMPLEMENTED. Redirects to registration.
        Button forgotButton = findViewById(R.id.forgotButton);
        forgotButton.setOnClickListener(view -> {
            startActivity(signupActivity);
            setCurrentActivity(signupActivity);
        });
    }


    /**
     * Once the user is logged in and the session has started, it redirects directly to the
     * homepage instead of the Login page
     */
    @Override
    protected void onStart(){
        super.onStart();

        if (session.isLoggedIn()) {
            moveToHomePage();
        }
    }


    /**
     * Regex to ensure that email is valid.
     *
     * @param email : The input email to be validated
     */
    protected boolean validateEmail(String email) {
        boolean emailFlag = false;

        Pattern emailPattern = Pattern.compile("^[.A-Za-z0-9]+@[A-Za-z0-9]+\\.[A-Za-z0-9]+$");
        Matcher emailMatcher = emailPattern.matcher(email);

        if (emailMatcher.find()) {emailFlag = true;}
        return emailFlag;
    }


    /**
     * Regex on a password string to determine if it is valid
     *
     * @param pass : The password plaintext
     * @return If the password is valid or not
     */
    protected boolean validatePass(String pass){
        boolean passFlag = false;

        // Mandatory, Minimum 8 Chars. Include at least one number
        Pattern passwordPattern = Pattern.compile("^(?=.*[a-zA-Z])(?=.*[a-zA-Z])(?=.*[0-9]).{8,}$");
        Matcher passwordMatcher = passwordPattern.matcher(pass);

        if (passwordMatcher.find()) {passFlag = true;}
        return passFlag;
    }


    /**
     * Uses FireBase Authentication to determine if the inputs (having passed regex)
     * exist in the database. The passwords comparison is already done behind the scenes.
     *
     * @param email : User email they use to log in
     * @param password : User password they use to log in
     */
    private void loginUserWithFirebase(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {

            // If the email and password are present in database then credentials are retrieved
            if (task.isSuccessful()) {
                retrieveCredentialsFromFirebase();
            }
            else{
                String error = "Credentials do not match";
                setStatusMessage(error);
            }
        });
    }


    /**
     * User data is retrieved from Firebase and retrieves the details if the dataSnapshot exists.
     * Then, a session is created with the user details which is accessed throughout the app.
     * Finally, the user is redirected to their respective homepage
     */
    private void retrieveCredentialsFromFirebase(){
        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance()
        .getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    UserInterface user = dataSnapshot.getValue(User.class);
                    String userHash = dataSnapshot.getKey();
                    fullName = user.getFirstName().concat(" ").concat(user.getLastName());
                    sessionEmail = user.getEmail();
                    sessionPass = user.getPassword();
                    phone = user.getPhone();
                    isEmp = user.getIsEmployee();

                    String error = "Credentials match";
                    setStatusMessage(error);

                    // Session is saved or created
                    session.createLoginSession(sessionEmail, sessionPass, fullName, isEmp, phone, userHash);
                    moveToHomePage();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    /**
     * After the session is created, it redirects to the homepage depending on he employee type
     */
    protected void moveToHomePage() {
        Intent intent;

        // sets the Intent depending on the Employee type
        if(session.getKeyIsEmployee().equalsIgnoreCase("Employee")) {
            intent = new Intent(this, EmployeeHomeActivity.class);
        }
        else {
            intent = new Intent(this, EmployerHomeActivity.class);
        }
        startActivity(intent);
        setCurrentActivity(intent);
    }

    /** This method is complete **/
    protected void setStatusMessage(String message) {
        TextView statusLabel = findViewById(R.id.loginErrorMessage);
        statusLabel.setText(message.trim());
    }

    @Override
    public void onBackPressed() {}
}
