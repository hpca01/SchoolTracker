package com.example.schooltracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.schooltracker.databinding.ActivityNewMentorBinding;
import com.example.schooltracker.models.entities.Mentor;
import com.example.schooltracker.models.entities.viewmodel.MentorViewModel;
import com.example.schooltracker.ui.NewMentorViewModel;

import java.util.List;
import java.util.Optional;

public class NewMentor extends AppCompatActivity {
    private NewMentorViewModel newMentorViewModel;
    private MentorViewModel mvm;
    private static final String TAG = "NewMentor";
    private int mentorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mvm = new MentorViewModel(getApplication());
        ActivityNewMentorBinding viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_new_mentor);

        Intent intent = getIntent();
        mentorId = intent.getIntExtra("mentor_id", -1);
        newMentorViewModel = new ViewModelProvider(this).get(NewMentorViewModel.class);
        viewDataBinding.setLifecycleOwner(this);
        viewDataBinding.setHandler(this);
        if (mentorId == -1){
            viewDataBinding.setNewMentorVM(newMentorViewModel);
        }else{
            viewDataBinding.setNewMentorVM(newMentorViewModel);
            getMentorData(mentorId);
        }
    }
    public void getMentorData(int mentorId){
        mvm.getAllMentors().observe(this, new Observer<List<Mentor>>() {
            @Override
            public void onChanged(List<Mentor> mentors) {
                Optional<Mentor> mentorToEdit = mentors.stream().filter(mentor -> mentor.getId() == mentorId).findFirst();
                mentorToEdit.ifPresent(mentor -> newMentorViewModel.setMentor(mentor));
            }
        });
    }



    public void validateForm(){
        if(!newMentorViewModel.isValidForm()){
            Toast.makeText(this, "PLease ensure mentor's name, email address, and phone number is filled out", Toast.LENGTH_SHORT).show();
        }else{
            Mentor m = newMentorViewModel.getMentor();
            if(mentorId == -1){
                mvm.insertMentor(m);
            }else{
                mvm.updateMentor(m);
            }
            Intent intent = new Intent(this, MentorsView.class);
            startActivity(intent);
            finish();
        }
    }
}
