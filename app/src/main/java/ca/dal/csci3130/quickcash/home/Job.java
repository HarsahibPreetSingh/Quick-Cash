package ca.dal.csci3130.quickcash.home;

import java.lang.reflect.Array;
import java.util.ArrayList;

import ca.dal.csci3130.quickcash.usermanagement.JobInterface;
import ca.dal.csci3130.quickcash.usermanagement.User;

public class Job implements JobInterface {
    //jobHash was added from US19 to help in identifying specific job when "Learn More" button
    // is clicked
    private String jobHash;
    private String jobTitle;
    private String jobLocation;
    private String jobWage;
    private String jobDuration;
    private String jobUrgency;
    private String jobDescription;
    private String employerName;

    // Will contain user hashes in string form so we can grab user object from DB
    private String employeePicked;
    private String jobOwnerHash;
    private ArrayList<String> jobApplications;

    public Job() {}

    public Job(String jobTitle, String jobLocation, String jobWage, String jobDuration,
               String jobUrgency, String jobDescription, String employerName,
               String employeePicked, String jobOwnerHash, ArrayList<String> jobApplications) {
        this.jobTitle = jobTitle;
        this.jobLocation = jobLocation;
        this.jobWage = jobWage;
        this.jobDuration = jobDuration;
        this.jobUrgency = jobUrgency;
        this.jobDescription = jobDescription;
        this.employerName = employerName;

        // Attach the employer user object to the job
        this.jobOwnerHash = jobOwnerHash;

        // Maximum 100 users can apply for a job. This will be updated after job is posted
        this.employeePicked = employeePicked;
        this.jobApplications = jobApplications;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobLocation() {
        return jobLocation;
    }

    public void setJobLocation(String jobLocation) {
        this.jobLocation = jobLocation;
    }

    public String getJobWage() {
        return jobWage;
    }

    public void setJobWage(String jobWage) {
        this.jobWage = jobWage;
    }

    public String getJobDuration() {
        return jobDuration;
    }

    public void setJobDuration(String jobDuration) {
        this.jobDuration = jobDuration;
    }

    public String getJobUrgency() {
        return jobUrgency;
    }

    public void setJobUrgency(String jobUrgency) {
        this.jobUrgency = jobUrgency;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getEmployeePicked() {
        return employeePicked;
    }

    public void setEmployeePicked(String employeePicked) {
        this.employeePicked = employeePicked;
    }

    public String getEmployerName() {
        return employerName;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    public String getJobOwnerHash() {
        return jobOwnerHash;
    }

    public void setJobOwnerHash(String employerHash) {
        this.jobOwnerHash = employerHash;
    }

    public ArrayList<String> getJobApplications() { return jobApplications; }

    public void setJobApplications(ArrayList<String> applications) {
        this.jobApplications = applications;
    }

    public void addJobApplication(String userHashApplication) {
        this.jobApplications.add(userHashApplication);
    }

    public String getJobHash() {
        return jobHash;
    }

    public void setJobHash(String jobHash) {
        this.jobHash = jobHash;
    }

}
