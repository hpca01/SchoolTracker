package com.example.schooltracker.models.entities.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import com.example.schooltracker.models.entities.Term;
import com.example.schooltracker.models.entities.TermWithCourses;
import com.example.schooltracker.models.repository.TermRepo;
import com.example.schooltracker.utils.CRUD;

import java.util.List;

public class TermViewModel extends AndroidViewModel implements CRUD<Term> {
    private TermRepo termRepo;
    private LiveData<List<Term>> allTerms;

    public TermViewModel(@NonNull Application app) {
        super(app);
        termRepo = new TermRepo(app);
        allTerms = termRepo.getListOfTerms();
    }

    public LiveData<List<Term>> getAllTerms() {
        return allTerms;
    }

    public LiveData<List<TermWithCourses>> getTermWithCourse(){
        return termRepo.fetchTermsWithCourses();
    }


    public void insertTerm(Term t){
        termRepo.insertTerm(t);
    }
    public void insertTerm(List<Term> t){
        termRepo.insertTerm(t);
    }
    public void deleteTerm(Term t){
        termRepo.deleteTerm(t);
    }
    public void updateTerm(Term t){
        termRepo.updateTerm(t);
    }

    @Override
    public void insert(Term t) {
        termRepo.insertTerm(t);
    }

    @Override
    public void delete(Term t) {
        termRepo.deleteTerm(t);
    }

    @Override
    public void update(Term t) {
        termRepo.updateTerm(t);
    }
}
