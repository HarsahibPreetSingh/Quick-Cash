package ca.dal.csci3130.quickcash.home;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.common.Constants;
import ca.dal.csci3130.quickcash.usermanagement.SessionManager;

public class JobRecyclerAdapter extends RecyclerView.Adapter<JobRecyclerAdapter.JobViewHolder> {
    ArrayList<Job> jobs;
    ViewGroup parent;

    public JobRecyclerAdapter(ArrayList<Job> jobs){
        this.jobs = jobs;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_card, parent, false);
        JobViewHolder jobViewHolder = new JobViewHolder(view);
        this.parent = parent;
        return jobViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        Job job = jobs.get(position);
        holder.jobTitle.setText(job.getJobTitle());
        holder.jobWage.setText(job.getJobWage());
        holder.jobLocation.setText(job.getJobLocation());
        // This is supposedly necessary on some of our machines, but it doesn't break anything.
        // We're keeping it just in case.
        //holder.position = holder.getBindingAdapterPosition();
        holder.job = job;
    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }

    public class JobViewHolder extends RecyclerView.ViewHolder {
        TextView jobTitle;
        TextView jobWage;
        TextView jobLocation;
        View rootView;
        int position;
        Job job;

        public JobViewHolder(@NonNull View itemView){
            super(itemView);
            rootView = itemView;
            jobTitle = itemView.findViewById(R.id.jobTitle);
            jobWage = itemView.findViewById(R.id.jobWage);
            jobLocation = itemView.findViewById(R.id.jobLocation);

            itemView.findViewById(R.id.learnMoreButton).setOnClickListener(view -> {
                SessionManager session = new SessionManager(parent.getContext());
                Intent intent;

                if(session.getKeyIsEmployee().equals("Employer"))
                    intent = new Intent(parent.getContext(), EmployerJobApplicationActivity.class);
                else
                    intent = new Intent(parent.getContext(), EmployeeJobApplicationActivity.class);

                intent.putExtra("jobHash", job.getJobHash());
                intent.putExtra("JobTitle", job.getJobTitle());
                intent.putExtra("jobLocation", job.getJobLocation());
                intent.putExtra("jobWage", job.getJobWage());
                intent.putExtra("jobDuration", job.getJobDuration());
                intent.putExtra("jobUrgency", job.getJobUrgency());
                intent.putExtra("jobDescription", job.getJobDescription());
                intent.putExtra("employerName", job.getEmployerName());
                intent.putExtra("employeePicked", job.getEmployeePicked());
                intent.putExtra("jobOwnerHash", job.getJobOwnerHash());
                intent.putExtra("jobApplications", job.getJobApplications());

                parent.getContext().startActivity(intent);
            });
        }
    }
}
