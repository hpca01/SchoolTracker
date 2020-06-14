package com.example.schooltracker.models.entities;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class CourseWithNotes {

    @Embedded
    public Course course;

    @Relation(parentColumn = "id", entityColumn = "courseId")
    public List<Note> notes;

}
