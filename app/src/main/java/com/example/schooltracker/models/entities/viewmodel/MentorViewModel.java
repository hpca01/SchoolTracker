package com.example.schooltracker.models.entities.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.schooltracker.models.entities.Mentor;
import com.example.schooltracker.models.repository.MentorRepo;

import java.util.List;

public class MentorViewModel extends AndroidViewModel {
    private MentorRepo mentorRepo;
    private LiveData<List<Mentor>> listOfMentors;

    public MentorViewModel(@NonNull Application application) {
        super(application);
        mentorRepo = new MentorRepo(application);
    }

    public LiveData<List<Mentor>> getAllMentors(){
        listOfMentors = mentorRepo.getListOfMentors();
        return listOfMentors;
    }
    public LiveData<List<Mentor>> getMentorByIds(List<Integer> mentorIds){
        return mentorRepo.getMentorsById(mentorIds);
    }

    public void insertMentor(Mentor ob){
        mentorRepo.insertMentor(ob);
    }
    
    public void updateMentor(Mentor ob){
        mentorRepo.updateMentor(ob);
    }
    
    public void deleteMentor(Mentor ob){
        mentorRepo.deleteMentor(ob);
    }
    
}
