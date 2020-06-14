package com.example.schooltracker.models.entities.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.schooltracker.models.entities.Assessment;
import com.example.schooltracker.models.repository.AssessmentRepo;
import com.example.schooltracker.utils.CRUD;

import java.util.List;

public class AssessmentViewModel extends AndroidViewModel implements CRUD<Assessment> {
    private AssessmentRepo assessmentRepo;
    private LiveData<List<Assessment>> assessmentByCourse;

    public AssessmentViewModel(@NonNull Application application) {
        super(application);
        assessmentRepo = new AssessmentRepo(application);
    }

    public LiveData<Assessment> getAssessmentById(int assessmentId){
        return assessmentRepo.getAssessmentById(assessmentId);
    }

    public LiveData<List<Assessment>> getAssessmentByCourse(int courseId){
        assessmentByCourse = assessmentRepo.getAssessmentByCourse(courseId);
        return assessmentByCourse;
    }
    public void insertAssessment(Assessment ob){
        assessmentRepo.insertAssessment(ob);
    }
    
    public void updateAssessment(Assessment ob){
        assessmentRepo.updateAssessment(ob);
    }
    
    public void deleteAssessment(Assessment ob){
        assessmentRepo.deleteAssessment(ob);
    }

    @Override
    public void insert(Assessment ob) {
        assessmentRepo.insertAssessment(ob);
    }

    @Override
    public void delete(Assessment ob) {
        assessmentRepo.deleteAssessment(ob);
    }

    @Override
    public void update(Assessment ob) {
        assessmentRepo.updateAssessment(ob);
    }
}
