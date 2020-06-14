package com.example.schooltracker.ui;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schooltracker.R;
import com.example.schooltracker.models.entities.Course;
import com.example.schooltracker.utils.Clickable;
import com.example.schooltracker.utils.Utils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.CourseViewHolder> {
    private static final String TAG = "CoursesAdapter";
    private List<Course> courses = new ArrayList<>();
    private Clickable clickable;
    private final LayoutInflater inflater;

    public CoursesAdapter(Context context, Clickable clickable) {
        inflater = LayoutInflater.from(context);
        this.clickable = clickable;
    }

    public void setCourses(List<Course> courses){
        this.courses = courses;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = this.inflater.inflate(R.layout.activity_term_details_course_recycler, parent, false);
        return new CourseViewHolder(v, clickable);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        if(!courses.isEmpty()){
            Course course = courses.get(position);
            holder.courseTitle.setText(course.getTitle());
            holder.setStartDate(course.getStart());
            holder.setEndDate(course.getEnd());
            holder.setCourseStatus(course.getStatus(), holder.itemView.getResources());
        }
    }


    @Override
    public int getItemCount() {
        return this.courses.size();
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        private Clickable clickable;

        @BindView(R.id.recycler_course_title)
        public TextView courseTitle;

        @BindView(R.id.recycler_course_start_date)
        public TextView startDate;

        @BindView(R.id.recycler_course_end_date)
        public TextView endDate;

        @BindView(R.id.recycler_course_status)
        public TextView courseStatus;

        public void setStartDate(Date startDate) {
            this.startDate.append(" "+Utils.getStringFromDate(startDate));
        }

        public void setEndDate(Date endDate) {
            this.endDate.append(" "+Utils.getStringFromDate(endDate));
        }

        public void setCourseStatus(Course.STATUS status, Resources res) {
            String statusString = Utils.getStatusString(status);
            courseStatus.setText(statusString);
            courseStatus.setBackgroundColor(Utils.getStatusColor(res, status));
        }

        public CourseViewHolder(@NonNull View itemView, Clickable clickable) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.clickable=clickable;
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
