package com.example.schooltracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;

import com.example.schooltracker.databinding.ActivityCourseAddMentorBinding;
import com.example.schooltracker.models.entities.CourseRelatedData;
import com.example.schooltracker.models.entities.CourseWithMentor;
import com.example.schooltracker.models.entities.Mentor;
import com.example.schooltracker.models.entities.viewmodel.CWMRelationViewModel;
import com.example.schooltracker.models.entities.viewmodel.CourseViewModel;
import com.example.schooltracker.models.entities.viewmodel.MentorViewModel;
import com.example.schooltracker.ui.CourseAddMentorAdapter;
import com.example.schooltracker.utils.Clickable;
import com.example.schooltracker.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CourseAddMentor extends AppCompatActivity implements Clickable {
    private static final String TAG = "CourseAddMentor";

    ActivityCourseAddMentorBinding courseAddMentorBinding;

    CWMRelationViewModel cwmRelationViewModel;

    CourseViewModel cvm;
    CourseRelatedData courseRelations; //get only courses by Id

    MentorViewModel mvm;
    List<Mentor> allMentors = new ArrayList<>(); //check marks get applied based on whether or not their ids are in courseRelations
    CourseAddMentorAdapter adapter;

    int courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cvm = new CourseViewModel(getApplication());
        mvm = new MentorViewModel(getApplication());
        cwmRelationViewModel = new CWMRelationViewModel(getApplication());

        courseAddMentorBinding = DataBindingUtil.setContentView(this, R.layout.activity_course_add_mentor);
        courseAddMentorBinding.setHandler(this);

        Intent intent = getIntent();
        courseId = intent.getIntExtra("course_id", -1);
        getAllCourseMentorRelations(courseId);

    }

    private void getAllCourseMentorRelations(int courseId){
        cvm.getAllRelatedDatabyCourse(courseId).observe(this, new Observer<CourseRelatedData>() {
            @Override
            public void onChanged(CourseRelatedData courseRelatedData) {
                courseRelations = courseRelatedData;
                List<CourseWithMentor> relations = courseRelations.mentorsAssociatedWithCourse;
                if (!relations.isEmpty()){
                    List<Integer> mentorIdsForLookup = relations.stream().map(CourseWithMentor::getMentorId).collect(Collectors.toList());
                    getAllMentors(mentorIdsForLookup);
                }else{
                    getAllMentors();
                }
            }
        });
    }

    private void getAllMentors(List<Integer> ids){
        mvm.getAllMentors().observe(this, new Observer<List<Mentor>>() {
            @Override
            public void onChanged(List<Mentor> mentors) {
                allMentors.addAll(mentors);
                allMentors.forEach(mentor -> {
                    if(ids.contains(mentor.getId())){
                        mentor.setChecked(Boolean.TRUE);
                    }
                });
                setupMentorsRecycler();
            }
        });
    }

    private void getAllMentors(){
        mvm.getAllMentors().observe(this, new Observer<List<Mentor>>() {
            @Override
            public void onChanged(List<Mentor> mentors) {
                allMentors.addAll(mentors);
                setupMentorsRecycler();
            }
        });
    }

    private void setupMentorsRecycler(){
        LinearLayoutManager layout = new LinearLayoutManager(this);
        Utils.addDividers(courseAddMentorBinding.mentorsRecycler, layout);
        courseAddMentorBinding.mentorsRecycler.setLayoutManager(layout);
        courseAddMentorBinding.mentorsRecycler.setHasFixedSize(true);

        adapter= new CourseAddMentorAdapter(getApplication(), this);
        courseAddMentorBinding.mentorsRecycler.setAdapter(adapter);
        adapter.setMentors(allMentors);
    }

    public void saveMentors(){
        //erase all relations first
        List<CourseWithMentor> current = courseRelations.mentorsAssociatedWithCourse;//need to delete all current ones then re-add new ones
        current.forEach(each->cwmRelationViewModel.deleteCWMRelation(each));

        Supplier<Stream<Mentor>> mentorsChecked = ()-> allMentors.stream().filter(Mentor::getChecked);
        if (mentorsChecked.get().count()>0){
            //if checked count is >0
            mentorsChecked.get().forEach(each->cwmRelationViewModel.insertCWMRelation(new CourseWithMentor(each.getId(), courseId)));
            Intent courseDetailsIntent = new Intent(this, CourseDetails.class);
            courseDetailsIntent.putExtra("course_id", courseId);
            startActivity(courseDetailsIntent);
            finish();
        }else{
            //do nothing
            Intent courseDetailsIntent = new Intent(this, CourseDetails.class);
            courseDetailsIntent.putExtra("course_id", courseId);
            startActivity(courseDetailsIntent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        Intent courseDetailsIntent = new Intent(this, CourseDetails.class);
        courseDetailsIntent.putExtra("course_id", courseId);
        startActivity(courseDetailsIntent);
        finish();
    }

    @Override
    public void clicked(int position) {

    }

    @Override
    public void longClick(int position) {

    }
}
