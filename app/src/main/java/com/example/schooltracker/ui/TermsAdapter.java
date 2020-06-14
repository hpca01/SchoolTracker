package com.example.schooltracker.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schooltracker.R;
import com.example.schooltracker.models.entities.Course;
import com.example.schooltracker.models.entities.Term;
import com.example.schooltracker.models.entities.TermWithCourses;
import com.example.schooltracker.utils.Clickable;
import com.example.schooltracker.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TermsAdapter extends RecyclerView.Adapter<TermsAdapter.TermsViewHolder> {
    private static final String TAG = "TermsAdapter";
    private List<TermWithCourses> terms = new ArrayList<>();
    private final LayoutInflater inflater;
    private Clickable clickable;

    public TermsAdapter(Context mContext, Clickable clickable) {
        inflater = LayoutInflater.from(mContext);
        this.clickable = clickable;
    }

    public void setTerms(List<TermWithCourses> terms){
        this.terms = terms;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TermsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = this.inflater.inflate(R.layout.activity_terms_view_recycler, parent, false);
        return new TermsViewHolder(v, this.clickable);
    }

    @Override
    public void onBindViewHolder(@NonNull TermsViewHolder holder, int position) {
        if (!terms.isEmpty()) {
            TermWithCourses term = terms.get(position);
            int courseCount = term.associatedCourses.size();
            int coursePlannedCount = (int) term.associatedCourses.stream().filter(each->each.getStatus() == Course.STATUS.PLANNED).count();
            int courseCompletedCount = (int) term.associatedCourses.stream().filter(each->each.getStatus() == Course.STATUS.COMPLETED).count();
            int courseProgressCount = (int) term.associatedCourses.stream().filter(each->each.getStatus() == Course.STATUS.IN_PROGRESS).count();
            int courseDroppedCount = (int) term.associatedCourses.stream().filter(each->each.getStatus() == Course.STATUS.DROPPED).count();

            holder.setTermTitle(term.term.getTitle());
            holder.setCourseCount(String.valueOf(courseCount));
            holder.setCoursePlanned(String.valueOf(coursePlannedCount));
            holder.setCourseCompleted(String.valueOf(courseCompletedCount));
            holder.setCourseInProgress(String.valueOf(courseProgressCount));
            holder.setCourseDropped(String.valueOf(courseDroppedCount));

            holder.setStartDate(term.term.getStart());
            holder.setEndDate(term.term.getEnd());
        }
    }


    @Override
    public int getItemCount() {
        return this.terms.size();
    }

    public static class TermsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private Clickable clickable;

        @BindView(R.id.recycler_term_title)
        public TextView termTitle;

        @BindView(R.id.recycler_term_course_count)
        public TextView courseCount;

        @BindView(R.id.recycler_term_course_planned)
        public TextView coursePlanned;

        @BindView(R.id.recycler_term_course_completed)
        public TextView courseCompleted;

        @BindView(R.id.recycler_term_course_in_progress)
        public TextView courseInProgress;

        @BindView(R.id.recycler_term_course_dropped)
        public TextView courseDropped;

        @BindView(R.id.recycler_term_start_date)
        public TextView startDate;

        @BindView(R.id.recycler_term_end_date)
        public TextView endDate;

        public void setTermTitle(String text) {
            this.termTitle.setText(text);
        }

        public void setCourseCount(String text) {
            this.courseCount.setText("# Courses: "+text);
        }

        public void setCoursePlanned(String text) {
            this.coursePlanned.setText(text);
        }

        public void setCourseCompleted(String text) {
            this.courseCompleted.setText(text);
        }

        public void setCourseInProgress(String text) {
            this.courseInProgress.setText(text);
        }

        public void setCourseDropped(String text) {
            this.courseDropped.setText(text);
        }

        public void setStartDate(Date startDate) {
            this.startDate.setText("Start "+Utils.getStringFromDate(startDate));
        }

        public void setEndDate(Date endDate) {
            this.endDate.setText("End "+Utils.getStringFromDate(endDate));
        }

        TermsViewHolder(@NonNull View itemView, Clickable clickable) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.clickable = clickable;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }
        @Override
        public void onClick(View v) {
            this.clickable.clicked(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            this.clickable.longClick(getAdapterPosition());
            return true;
        }
    }
}
