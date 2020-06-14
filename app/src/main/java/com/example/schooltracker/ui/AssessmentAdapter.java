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
import com.example.schooltracker.models.entities.Assessment;
import com.example.schooltracker.utils.CombinedRecyclerView;
import com.example.schooltracker.utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.AssesmentViewHolder> {
    private static final String TAG = "AssessmentAdapter";
    private List<Assessment> assessmentList = new ArrayList<>();
    private final LayoutInflater inflater;
    private CombinedRecyclerView clickable;

    public AssessmentAdapter(Context context, CombinedRecyclerView clickable) {
        inflater = LayoutInflater.from(context);
        this.clickable = clickable;
    }

    public void setAssessmentList(List<Assessment> assessmentList) {
        this.assessmentList = assessmentList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AssessmentAdapter.AssesmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = this.inflater.inflate(R.layout.assessment_list_recycler, parent, false);
        return new AssesmentViewHolder(v, this.clickable);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentAdapter.AssesmentViewHolder holder, int position) {
        if (!this.assessmentList.isEmpty()){
            Assessment assessment = this.assessmentList.get(position);
            holder.title.setText(assessment.getTitle());
            holder.type.setText(assessment.getType().toString());
            holder.setStartDate(assessment.getStart());
            holder.setEndDate(assessment.getEnd());
            holder.setAlert(assessment.isAlert(), holder.itemView.getResources());
        }
    }

    @Override
    public int getItemCount() {
        return assessmentList.size();
    }

    public class AssesmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        private final CombinedRecyclerView clickable;
        @BindView(R.id.titleOfAssessment)
        public TextView title;

        @BindView(R.id.assessmentType)
        public TextView type;

        @BindView(R.id.startDate)
        public TextView startDate;

        @BindView(R.id.endDate)
        public TextView endDate;

        @BindView(R.id.alertSet)
        public TextView alert;

        public void setStartDate(Date date) {
            this.startDate.setText(Utils.getStringFromDate(date));
        }

        public void setEndDate(Date date) {
            this.endDate.setText(Utils.getStringFromDate(date));
        }

        public void setAlert(boolean alertSet, Resources resources){
            if (alertSet){
                alert.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.courseCompleted, null));
            }else{
                alert.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.courseDropped, null));
            }
        }

        public AssesmentViewHolder(@NonNull View itemView, CombinedRecyclerView clickable) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.clickable = clickable;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickable.assessmentClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            clickable.assessmentLongClick(getAdapterPosition());
            return true;
        }
    }
}
