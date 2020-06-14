package com.example.schooltracker.ui;

import android.util.Log;

import androidx.databinding.Bindable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.schooltracker.models.entities.Mentor;

public class NewMentorViewModel extends ViewModel {
    Mentor m = null;
    private static final String TAG = "NewMentorViewModel";

    public MutableLiveData<String> name = new MutableLiveData<>();

    public MutableLiveData<String> email = new MutableLiveData<>();

    public MutableLiveData<String> phoneNumber = new MutableLiveData<>();

    //return true if valid
    public boolean isValidForm() {
        return name.getValue() != null && email.getValue() != null && phoneNumber.getValue() != null && !name.getValue().isEmpty() && !email.getValue().isEmpty() && !phoneNumber.getValue().isEmpty();
    }

    public Mentor getMentor(){
        if (this.m == null){
            this.m = new Mentor(name.getValue(), email.getValue(), phoneNumber.getValue());
        }
        else{
            this.m.setName(name.getValue());
            this.m.setEmail(email.getValue());
            this.m.setPhoneNumber(phoneNumber.getValue());
        }
        return this.m;
    }

    public void setMentor(Mentor m){
        name.setValue(m.getName());
        email.setValue(m.getEmail());
        phoneNumber.setValue(m.getPhoneNumber());
        this.m = m;
    }

}
