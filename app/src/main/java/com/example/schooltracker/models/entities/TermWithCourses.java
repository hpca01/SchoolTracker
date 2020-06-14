package com.example.schooltracker.models.entities;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class TermWithCourses {

    @Embedded
    public Term term;

    @Relation(parentColumn = "id", entityColumn = "termId")
    public List<Course> associatedCourses;
}
