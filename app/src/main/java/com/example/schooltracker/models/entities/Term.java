package com.example.schooltracker.models.entities;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;

@Entity(tableName = "term")
public class Term {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NotNull
    private String title;

    @NotNull
    private Date start;

    @NotNull
    private Date end;

    @ColumnInfo(defaultValue = "false")
    private boolean alert;

    @Ignore
    private LiveData<List<Course>> associatedCourses;

    public Term(String title, Date start, Date end) {
        this.title = title;
        this.start = start;
        this.end = end;
    }

    public boolean isAlert() {
        return alert;
    }

    public void setAlert(boolean alert) {
        this.alert = alert;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public LiveData<List<Course>> getAssociatedCourses() {
        return associatedCourses;
    }

    public void setAssociatedCourses(LiveData<List<Course>> associatedCourses) {
        this.associatedCourses = associatedCourses;
    }
}
