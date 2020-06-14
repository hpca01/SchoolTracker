package com.example.schooltracker.models.entities.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.schooltracker.models.entities.CourseWithMentor;
import com.example.schooltracker.models.repository.CourseWithMentorRelationRepo;

public class CWMRelationViewModel extends AndroidViewModel {
    private CourseWithMentorRelationRepo CWMRepo;


    public CWMRelationViewModel(@NonNull Application application) {
        super(application);
        this.CWMRepo = new CourseWithMentorRelationRepo(application);
    }

    public void insertCWMRelation(CourseWithMentor ob){
        this.CWMRepo.insertCourseWithMentor(ob);
    }

    public void updateCWMRelation(CourseWithMentor ob){
        this.CWMRepo.updateCourseWithMentor(ob);
    }

    public void deleteCWMRelation(CourseWithMentor ob){
        this.CWMRepo.deleteCourseWithMentor(ob);
    }
}
