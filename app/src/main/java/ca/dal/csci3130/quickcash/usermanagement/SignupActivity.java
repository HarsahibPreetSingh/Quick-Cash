package ca.dal.csci3130.quickcash.usermanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.dal.csci3130.quickcash.R;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText registerFirstName, registerLastName, registerPassword, confirmPassword;
    private EditText registerEmail, registerPhoneNumber;

    private String firstName, lastName, password, confirmPass, phoneNumber, email, userType;

    private Button registerUserButton, goToLogin;
    private RadioGroup radiogroup;
    private RadioButton radiobutton;

    // FIREBASE
    private DatabaseReference getFirebaseDBRef;
    private static FirebaseAuth mAuth;


    /**
     * Initializes frontend objects on signup page to extract information.
     * Setting onClickListeners for buttons to redirect to certain pages.
     * Also handles signup logic, and talking to the database
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        radiogroup = findViewById(R.id.radioGroup);
        this.initializeDB();

        registerFirstName = findViewById(R.id.registerFirstName);
        registerLastName = findViewById(R.id.registerLastName);
        registerPassword = findViewById(R.id.registerPassword);
        confirmPassword = findViewById(R.id.confirmPassword);
        registerEmail = findViewById(R.id.registerEmail);
        registerPhoneNumber = findViewById(R.id.registerPhoneNumber);
        registerUserButton = findViewById(R.id.registerButton);

        radiobutton = findViewById(radiogroup.getCheckedRadioButtonId());
        userType = radiobutton.getText().toString();

        // Code for directing user back to login page
        goToLogin = findViewById(R.id.backToLogin);
        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }

        });

        registerUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Extracting the data from the form
                firstName = registerFirstName.getText().toString();
                lastName = registerLastName.getText().toString();
                email = registerEmail.getText().toString();
                password = registerPassword.getText().toString();
                confirmPass = confirmPassword.getText().toString();
                phoneNumber = registerPhoneNumber.getText().toString();
                userType = checkButton(view);

                // Validate all the other fields, and register user if everything checks out
                if (verifyDetails(firstName, lastName, password, confirmPass, phoneNumber, email)) {
                    attemptToRegisterUser();
                }
            }
        });
    }

    /**
     * Checks to see the email has not already been registered. If not, a user object
     * is created and posted to the database to be stored.
     *
     * This method assumes the view has full and complete information when being called
     */
    protected void attemptToRegisterUser() {
        mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(task -> {
            // Create a new user if the email has not been registered before
            if (task.getResult().getSignInMethods().size() == 0) {
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Toast.makeText(
                                SignupActivity.this,
                                "Task was successful",
                                Toast.LENGTH_LONG).show();

                        // creating a User class object and passing it to Firebase to make a database entry
                        registerAndMoveToLogin(new User(firstName, lastName, email, phoneNumber, userType));
                    } else {
                        Toast.makeText(
                                SignupActivity.this,
                                "Failed to register",
                                Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                registerEmail.setError("Email already exists");
            }
        });
    }


    /**
     * Checks to see the email has not already been registered. If not, a user object
     * is created and posted to the database to be stored.
     *
     * This method assumes the view has full and complete information when being called
     */
    protected void registerAndMoveToLogin(User user) {
        getFirebaseDBRef.child(mAuth.getCurrentUser().getUid()).setValue(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(
                    SignupActivity.this,
                    "User has been registered",
                    Toast.LENGTH_LONG).show();

                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
            }
            else {
                Toast.makeText(
                    SignupActivity.this,
                    "Failed to register try again",
                    Toast.LENGTH_LONG).show();
            }
        });
    }

    public void initializeDB() {
        mAuth = FirebaseAuth.getInstance();
        getFirebaseDBRef = FirebaseDatabase.getInstance().getReference("Users");
    }

    /**
     * Verify that all information inputted on the registration is completely filled
     * and fulfills all regex requirements.
     *
     * @param firstName
     * @param lastName
     * @param password    : Plaintext
     * @param confirmPass : Plaintext
     * @param phoneNumber : XXX-XXX-XXXX
     * @param email
     * @return true if all details are complete and valid
     */
    protected boolean verifyDetails(String firstName, String lastName, String password,
                                    String confirmPass, String phoneNumber, String email) {
        boolean detailsVerified = true;

        if (!isValidName(firstName)) {
            detailsVerified = false;
            registerFirstName.setError("Name field invalid");
        }
        if (!isValidName(lastName)) {
            registerLastName.setError("Name field invalid");
        }
        if (!passwordsMatch(password, confirmPass)) {
            detailsVerified = false;
            confirmPassword.setError("Passwords do not match");
        }
        if (password.isEmpty()) {
            registerPassword.setError("Password cannot be empty");
        }
        if (confirmPass.isEmpty()) {
            confirmPassword.setError("Password cannot be empty");
        }
        if (!isValidPhoneNumber(phoneNumber)) {
            detailsVerified = false;
            registerPhoneNumber.setError("Format: XXX-XXX-XXXX");
        }

        if (!isValidEmail(email)) {
            detailsVerified = false;
            registerEmail.setError("Email invalid");
            if (email.trim().isEmpty()) {
                registerEmail.setError("Email cannot be empty");
            }
        }
        return detailsVerified;
    }


    public String checkButton(View view) {
        radiobutton = findViewById(radiogroup.getCheckedRadioButtonId());
        return radiobutton.getText().toString();
    }

    /**
     * Adds a user interface to the UserDAO. The userDAO is used for
     * getting, updating, and removing user objects
     */
    protected void addUser(UserInterface userInterface) {
        UserDAO userDAO = new UserDAO();
        userDAO.add(userInterface);
    }

    /**
     * Regex to ensure that email is valid
     *
     * @param email : The input email to be validated
     */
    protected boolean isValidEmail(String email) {
        // Check if it's a valid email. Simple Regex.
        Pattern validNetIDPattern = Pattern.compile("^[.A-Za-z0-9]+@[A-Za-z0-9]+\\.[A-Za-z0-9]+$");
        Matcher validNetIdMatcher = validNetIDPattern.matcher(email);
        boolean matchFound = validNetIdMatcher.find();

        if (matchFound) {
            return true;
        }
        return false;
    }

    /**
     * Regex to ensure that a single name (Firstname, or Lastname)
     *
     * @param name : A single word. You call this method twice+ for a full name
     */
    protected boolean isValidName(String name) {
        // Only allow letters allowed. Resource used:
        // https://www.w3schools.com/java/java_regex.asp
        Pattern validNamePattern = Pattern.compile("^[A-Za-z]+$");
        Matcher validNameMatcher = validNamePattern.matcher(name);

        if (validNameMatcher.find()) {
            return true;
        }
        return false;

    }

    /**
     * Checks that the "password" and "confirm password" fields match
     *
     * @return true if fields match
     */
    protected boolean passwordsMatch(String pwOne, String pwTwo) {
        if (pwOne.equals(pwTwo)) {
            return true;
        }
        return false;
    }

    /**
     * Regex to ensure that the phone number is valid
     *
     * @param phoneNumber : formatted XXX-XXX-XXXX, where X is a number
     * @return true if phone number follows formatting
     */
    protected boolean isValidPhoneNumber(String phoneNumber) {
        Pattern validNetIDPattern = Pattern.compile("^[0-9]{3}-[0-9]{3}-[0-9]{4}$");
        Matcher validNetIdMatcher = validNetIDPattern.matcher(phoneNumber);
        boolean matchFound = validNetIdMatcher.find();

        if (matchFound) {
            return true;
        }
        return false;
    }

    /**
     * Regex to ensure that the password is valid
     *
     * @param password : formatted XXX-XXX-XXXX, where X is a number
     * @return true if password is at least 8 characters and contains at least one number.
     */
    protected boolean passwordValid(String password) {
        // Minimum 8 characters, at least one number
        Pattern validNetIDPattern = Pattern.compile("^(?=.*[a-zA-Z])(?=.*[a-zA-Z])(?=.*[0-9]).{8,}$");
        Matcher validNetIdMatcher = validNetIDPattern.matcher(password);
        boolean matchFound = validNetIdMatcher.find();

        if (matchFound) {
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View view) {

    }
}