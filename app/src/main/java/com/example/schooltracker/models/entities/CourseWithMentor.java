package com.example.schooltracker.models.entities;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;


import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = {@ForeignKey(entity = Course.class, parentColumns = "id", childColumns = "courseId", onDelete = CASCADE),
        @ForeignKey(entity = Mentor.class, parentColumns = "id", childColumns = "mentorId", onDelete = CASCADE)})
public class CourseWithMentor {
    @PrimaryKey(autoGenerate = true)
    private int id;


    private int mentorId;


    private int courseId;

    public CourseWithMentor(int mentorId, int courseId) {
        this.mentorId = mentorId;
        this.courseId = courseId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMentorId() {
        return mentorId;
    }

    public void setMentorId(int mentorId) {
        this.mentorId = mentorId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    @Override
    public String toString() {
        return "CourseWithMentor{" +
                "id=" + id +
                ", mentorId=" + mentorId +
                ", courseId=" + courseId +
                '}';
    }
}
