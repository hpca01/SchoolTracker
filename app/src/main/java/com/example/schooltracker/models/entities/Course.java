package com.example.schooltracker.models.entities;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.List;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "course", foreignKeys = {@ForeignKey(entity = Term.class, parentColumns = "id", childColumns = "termId", onDelete = CASCADE)})
public class Course {

    public enum STATUS{
        IN_PROGRESS(0),
        COMPLETED(1),
        PLANNED(2),
        DROPPED(3);

        private final int value;

        STATUS(final int newValue){
            value=newValue;
        }
        public int getValue(){
            return value;
        }

        public String toString(){
            return this.name();
        }
    }

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private Date start;
    private Date end;
    private boolean alert;
    private STATUS status;


    private int termId;

    @Ignore
    private List<Note> notes;

    @Ignore
    private List<Assessment> assessmentList;

    @Ignore
    private Mentor mentor;

    public int getTermId() {
        return termId;
    }

    public void setTermId(int termId) {
        this.termId = termId;
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

    public boolean isAlert() {
        return alert;
    }

    public void setAlert(boolean alert) {
        this.alert = alert;
    }

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    @Ignore
    public List<Note> getNotes() {
        return notes;
    }

    @Ignore
    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public List<Assessment> getAssessmentList() {
        return assessmentList;
    }

    public void setAssessmentList(List<Assessment> assessmentList) {
        this.assessmentList = assessmentList;
    }

    public Mentor getMentor() {
        return mentor;
    }

    public void setMentor(Mentor mentor) {
        this.mentor = mentor;
    }

    public Course(String title, Date start, Date end, boolean alert, STATUS status) {
        this.title = title;
        this.start = start;
        this.end = end;
        this.alert = alert;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", alert=" + alert +
                ", status=" + status +
                ", termId=" + termId +
                '}';
    }
}
