package com.example.schooltracker.models.entities;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class CourseRelatedData {

    @Embedded
    public Course course;

    @Relation(parentColumn = "id", entityColumn = "courseId")
    public List<Assessment> assessments;

    @Relation(parentColumn = "id", entityColumn = "courseId")
    public List<Note> notes;

    @Relation(parentColumn = "id", entityColumn = "courseId")
    public List<CourseWithMentor> mentorsAssociatedWithCourse;

}
