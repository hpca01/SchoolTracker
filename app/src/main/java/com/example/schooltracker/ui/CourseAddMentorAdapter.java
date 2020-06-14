package com.example.schooltracker.ui;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schooltracker.R;
import com.example.schooltracker.databinding.ActivityCourseAddMentorRecyclerBinding;
import com.example.schooltracker.models.entities.Mentor;
import com.example.schooltracker.utils.Clickable;

import java.util.ArrayList;
import java.util.List;

public class CourseAddMentorAdapter extends RecyclerView.Adapter<CourseAddMentorAdapter.CMAddViewHolder> {
    private static final String TAG = "CourseAddMentorAdapter";
    
    private List<Mentor> mentors = new ArrayList<>();
    private final LayoutInflater inflater;
    private Clickable clickable;

    public CourseAddMentorAdapter(Context context, Clickable clickable) {
        this.inflater = LayoutInflater.from(context);
        this.clickable = clickable;
    }

    public void setMentors(List<Mentor> mentors){
        this.mentors = mentors;
        notifyDataSetChanged();
    }

    public List<Mentor> getMentors(){
        return this.mentors;
    }

    @NonNull
    @Override
    public CMAddViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivityCourseAddMentorRecyclerBinding binding = DataBindingUtil.inflate(this.inflater, R.layout.activity_course_add_mentor_recycler, parent, false);
        return new CMAddViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CMAddViewHolder holder, int position) {
        Mentor m = this.mentors.get(position);
        holder.addMentorRecyclerBinding.setMentor(m);
    }

    @Override
    public int getItemCount() {
        return this.mentors.size();
    }

    public static class CMAddViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ActivityCourseAddMentorRecyclerBinding addMentorRecyclerBinding;

        public CMAddViewHolder(@NonNull ActivityCourseAddMentorRecyclerBinding itemView) {
            super(itemView.getRoot());
            addMentorRecyclerBinding = itemView;
            addMentorRecyclerBinding.mentorCheckbox.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            Log.d(TAG, "onClick: mentor list "+pos);
        }
    }
}
