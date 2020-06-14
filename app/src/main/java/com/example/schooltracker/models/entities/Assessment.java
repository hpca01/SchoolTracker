package com.example.schooltracker.models.entities;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = {@ForeignKey(entity = Course.class, parentColumns = "id", childColumns = "courseId", onDelete = CASCADE)})
public class Assessment {

    public enum TYPE{
        PERFORMANCE(0),
        OBJECTIVE(1);
        private final int value;

        TYPE(final int newValue){
            value = newValue;
        }
        public int getValue(){
            return value;
        }
    }

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;

    private Date start;
    private Date end;
    private TYPE type;
    private boolean alert;


    private int courseId;

    public Assessment(String title, Date start, Date end, TYPE type, boolean alert) {
        this.title = title;
        this.start = start;
        this.end = end;
        this.type = type;
        this.alert = alert;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
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

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public boolean isAlert() {
        return alert;
    }

    public void setAlert(boolean alert) {
        this.alert = alert;
    }

    @Override
    public String toString() {
        return "Assessment{" +
                "id=" + id +
                ", start=" + start +
                ", end=" + end +
                ", type=" + type +
                ", alert=" + alert +
                ", courseId=" + courseId +
                '}';
    }
}
