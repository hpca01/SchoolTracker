package com.example.schooltracker;

import androidx.annotation.LongDef;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.schooltracker.models.entities.CourseRelatedData;
import com.example.schooltracker.models.entities.Term;
import com.example.schooltracker.models.entities.TermWithCourses;
import com.example.schooltracker.models.entities.viewmodel.CourseViewModel;
import com.example.schooltracker.models.entities.viewmodel.TermViewModel;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    private Button termsBtn;
    private Button mentorBtn;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        termsBtn = (Button) findViewById(R.id.termsBtn);
        termsBtn.setOnClickListener((view) -> openTerms());

        mentorBtn = (Button) findViewById(R.id.mentorsBtn);
        mentorBtn.setOnClickListener((view) -> openMentors());

        getCourseData(1);
    }

    public void getTermsWithCourses() {
        String TAG = "Terms with courses";
        TermViewModel tvm = new TermViewModel(getApplication());
        tvm.getTermWithCourse().observe(this, new Observer<List<TermWithCourses>>() {
            @Override
            public void onChanged(List<TermWithCourses> termWithCourses) {
                Log.d(TAG, "onChanged: getting list");
                termWithCourses.stream().forEach(each -> Log.d(TAG, "onChanged: " + each.term.getTitle() + " " + each.associatedCourses.toString()));
            }
        });
    }

    public void getCourseData(int courseId) {
        CourseViewModel cvm = new CourseViewModel(getApplication());
        cvm.getAllRelatedDatabyCourse(1).observe(this, new Observer<CourseRelatedData>() {
            @Override
            public void onChanged(CourseRelatedData courseRelatedData) {
                Log.d(TAG, "onChanged: " + courseRelatedData);
                if (courseRelatedData!=null){CourseRelatedData each = courseRelatedData;
                Log.d(TAG, "onChanged: course " + each.toString());
                each.assessments.forEach(data -> Log.d(TAG, "onChanged: " + data.toString()));
                each.notes.forEach(note -> Log.d(TAG, "onChanged: note " + note.toString()));
                each.mentorsAssociatedWithCourse.forEach(mentor -> Log.d(TAG, "onChanged: " + mentor.toString()));}
            }
        });
    }

    public void openTerms() {
        //open terms
        Intent termsIntent = new Intent(this, TermsView.class);
        startActivity(termsIntent);
    }

    public void openMentors() {
        Intent mentorIntent = new Intent(this, MentorsView.class);
        startActivity(mentorIntent);
    }
}
