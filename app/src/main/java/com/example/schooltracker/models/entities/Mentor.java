package com.example.schooltracker.models.entities;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "mentor")
public class Mentor {

    @Ignore
    private static final String TAG = "Mentor";

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String email;
    private String phoneNumber;

    @Ignore
    private MutableLiveData<Boolean> checked = new MutableLiveData<>(false);

    public Boolean getChecked() {
        return checked.getValue();
    }

    public void setChecked(Boolean checked) {
        this.checked.setValue(checked);
    }

    public Mentor(String name, String email, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return this.name+" "+this.phoneNumber;
    }
}
