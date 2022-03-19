package ca.dal.csci3130.quickcash.usermanagement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager implements SessionManagerInterface {

    // Shared Preferences reference
    SharedPreferences pref;

    // Editor reference for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREFER_NAME = "loginSession";

    // All Shared Preferences Keys
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    // Email address (make variable public to access from outside)
    public static final String KEY_PASS = "password";

    public static final String KEY_EMPLOYEE = "isEmployee";

    public static final String KEY_PHONE = "phone";

    public static final String USER_HASH = "userhash";

    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    @Override
    public void createLoginSession(String email, String password, String name, String isEmployee, String phone, String userHash) {
        // Storing login value as TRUE
        editor.putBoolean(IS_USER_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_NAME, name);

        // Storing email in pref
        editor.putString(KEY_EMAIL, email);

        // Storing password in pref
        editor.putString(KEY_PASS, password);

        // Storing isEmployee in pref
        editor.putString(KEY_EMPLOYEE, isEmployee);

        // Storing phone in pref
        editor.putString(KEY_PHONE, phone);

        // Placing the user hash in the session to query the database to get current user
        editor.putString(USER_HASH, userHash);

        // commit changes
        editor.commit();
    }

    @Override
    public void checkLogin() {

    }


    @Override
    public void logoutUser() {
        // Clearing all user data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Login Activity
        Intent i = new Intent(_context, LoginActivity.class);

        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }


    @Override
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_USER_LOGIN, false);
    }

    @Override
    public String getKeyName() {
        return pref.getString(KEY_NAME, "No Name is Found");
    }

    @Override
    public String getKeyEmail() {
        return pref.getString(KEY_EMAIL, "No Email is Found");
    }

    @Override
    public String getKeyIsEmployee() {
        return pref.getString(KEY_EMPLOYEE, "No EmployeeType is Found");
    }

    @Override
    public String getKeyPhone() {
        return pref.getString(KEY_PHONE, "No PhoneNo. is Found");
    }

    @Override
    public String getKeyPass() {
        return pref.getString(KEY_PASS, "No Password is Found");
    }

    @Override
    public String getUserHash() { return pref.getString(USER_HASH, "No user hash is found"); }
}
