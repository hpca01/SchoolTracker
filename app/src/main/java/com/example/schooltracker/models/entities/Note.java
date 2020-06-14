package com.example.schooltracker.models.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "note", foreignKeys = {@ForeignKey(entity = Course.class, parentColumns = "id", childColumns = "courseId", onDelete = CASCADE)})
public class Note {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String noteText;


    private int courseId;

    public Note(String noteText) {
        this.noteText = noteText;
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

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }
}
