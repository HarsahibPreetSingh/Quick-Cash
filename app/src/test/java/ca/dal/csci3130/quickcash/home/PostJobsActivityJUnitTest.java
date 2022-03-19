package ca.dal.csci3130.quickcash.home;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.mockito.Mockito;

import ca.dal.csci3130.quickcash.usermanagement.JobDAO;
import ca.dal.csci3130.quickcash.usermanagement.SignupActivity;
import ca.dal.csci3130.quickcash.usermanagement.UserDAO;
import ca.dal.csci3130.quickcash.usermanagement.UserInterface;

public class PostJobsActivityJUnitTest {

    static PostJobsActivity postjobsActivity;
    PostJobsActivity postjobsActivity1;

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Before
    public void setup() {
        postjobsActivity = new PostJobsActivity();
        postjobsActivity1 = Mockito.mock(PostJobsActivity.class);
        JobDAO jobDAO = Mockito.mock(JobDAO.class);
        when(jobDAO.add(any(UserInterface.class))).thenReturn(null);
        doNothing().when(postjobsActivity1).addJob(any());
    }


}
