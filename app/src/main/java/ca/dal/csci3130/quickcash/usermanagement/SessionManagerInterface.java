package ca.dal.csci3130.quickcash.usermanagement;

public interface SessionManagerInterface {

    void createLoginSession(String email, String password, String name, String isEmployee, String phone, String userHash);

    void checkLogin();

    void logoutUser();

    boolean isLoggedIn();

    String getKeyName();

    String getKeyEmail();

    String getKeyIsEmployee();

    String getKeyPhone();

    String getKeyPass();

    String getUserHash();
}
