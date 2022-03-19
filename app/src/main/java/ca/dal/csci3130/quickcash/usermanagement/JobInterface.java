package ca.dal.csci3130.quickcash.usermanagement;

public interface JobInterface {

    String getJobTitle();

    void setJobTitle(String jobTitle);

    String getJobLocation();

    void setJobLocation(String jobLocation);

    String getJobWage();

    void setJobWage(String jobWage);

    String getJobDuration();

    void setJobDuration(String jobDuration);

    String getJobUrgency();

    void setJobUrgency(String jobUrgency);

    String getJobDescription();

    void setJobDescription(String jobDescription);

    String getEmployeePicked();

    void setEmployeePicked(String employeePicked);

    String getEmployerName();

    void setEmployerName(String employerName);

    String getJobHash();

    void setJobHash(String jobHash);
}
