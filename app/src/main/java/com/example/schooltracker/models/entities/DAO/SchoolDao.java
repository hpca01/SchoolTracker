package com.example.schooltracker.models.entities.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.schooltracker.models.entities.Assessment;
import com.example.schooltracker.models.entities.Course;
import com.example.schooltracker.models.entities.CourseRelatedData;
import com.example.schooltracker.models.entities.CourseWithMentor;
import com.example.schooltracker.models.entities.Mentor;
import com.example.schooltracker.models.entities.Note;
import com.example.schooltracker.models.entities.Term;
import com.example.schooltracker.models.entities.TermWithCourses;

import java.util.List;

@Dao
public interface SchoolDao {
    //term
    @Insert
    void insertTerm(Term term);

    @Delete
    void deleteTerm(Term term);

    @Update
    void updateTerm(Term term);

    @Query("Select * from term")
    LiveData<List<Term>> getTermsNoCourses();

    @Transaction
    @Query("Select * from term")
    LiveData<List<TermWithCourses>> loadTerms();

    //courses
    @Insert
    long insertCourse(Course course);

    @Delete
    void deleteCourse(Course course);

    @Update
    void updateCourse(Course course);

    @Transaction
    @Query("Select * from course where course.id=:courseId")
    LiveData<CourseRelatedData> loadCourseWithNotes(final int courseId);

    @Query("Select * from course where termId=:termId")
    LiveData<List<Course>> getCoursesById(final int termId);

    //assessment
    @Insert
    void insertAssessment(Assessment assessment);

    @Delete
    void deleteAssessment(Assessment assessment);

    @Update
    void updateAssessment(Assessment assessment);

    @Query("Select * from assessment where courseId=:courseId")
    LiveData<List<Assessment>> findAssessmentByCourse(int courseId);

    @Query("Select * from assessment where id=:assessmentId")
    LiveData<Assessment> getAssessmentById(int assessmentId);

    //note
    @Insert
    void insertNote(Note note);

    @Delete
    void deleteNote(Note note);

    @Update
    void updateNote(Note note);

    @Query("Select * from note where courseId=:courseId")
    LiveData<List<Note>> findNotesByCourse(int courseId);

    @Query("Select * from note where id=:noteId")
    LiveData<Note> findNoteById(int noteId);

    //mentor
    @Insert
    void insertMentor(Mentor mentor);

    @Delete
    void deleteMentor(Mentor mentor);

    @Update
    void updateMentor(Mentor mentor);

    @Query("Select * from mentor")
    LiveData<List<Mentor>> getAllMentors();

    @Query("Select * from mentor WHERE id in (:mentorIds)")
    LiveData<List<Mentor>> getMentorsById(List<Integer> mentorIds);

    //course with mentor --many to many
    @Insert
    void insertCourseWithMentor(CourseWithMentor courseWithMentor);

    @Delete
    void deleteCourseWithMentor(CourseWithMentor courseWithMentor);

    @Update
    void updateCourseWithMentor(CourseWithMentor courseWithMentor);

    @Query("Select * from CourseWithMentor")
    LiveData<List<CourseWithMentor>> allMentorCourseData();
}
