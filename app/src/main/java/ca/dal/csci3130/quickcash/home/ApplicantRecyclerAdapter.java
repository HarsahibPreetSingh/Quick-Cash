package ca.dal.csci3130.quickcash.home;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ca.dal.csci3130.quickcash.R;

public class ApplicantRecyclerAdapter extends RecyclerView.Adapter<ApplicantRecyclerAdapter.ApplicantViewHolder> {
    ArrayList<String[]> applicants;
    ViewGroup parent;

    public ApplicantRecyclerAdapter(ArrayList<String[]> applicants){
        this.applicants = applicants;
    }

    @NonNull
    @Override
    public ApplicantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_applicant_card, parent, false);
        ApplicantViewHolder applicantViewHolder = new ApplicantViewHolder(view);
        this.parent = parent;
        return applicantViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ApplicantViewHolder holder, int position) {
        String[] applicant = applicants.get(position);
        holder.applicantFullName.setText(applicant[0]);
        holder.applicantEmail.setText(applicant[1]);

        // Needs to be fully implemented once the rating system is implemented
        //---------------------------------------------------------------------------------------------
        holder.applicantRating.setText(applicant[2]);

        holder.position = holder.getBindingAdapterPosition();
        holder.applicant = applicant;
    }

    @Override
    public int getItemCount() {
        return applicants.size();
    }

    public class ApplicantViewHolder extends RecyclerView.ViewHolder {
        TextView applicantFullName;
        TextView applicantEmail;
        TextView applicantRating;
        View rootView;
        int position;
        String[] applicant;

        public ApplicantViewHolder(@NonNull View itemView){
            super(itemView);
            rootView = itemView;
            applicantFullName = itemView.findViewById(R.id.applicantName);
            applicantEmail = itemView.findViewById(R.id.applicantEmail);
            applicantRating = itemView.findViewById(R.id.applicantRating);

            itemView.findViewById(R.id.acceptButton).setOnClickListener(view -> {
                // The accept button functionality needs to be implemented
                //---------------------------------------------------------------------------------------------
            });
        }
    }
}
