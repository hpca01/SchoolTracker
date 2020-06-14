package com.example.schooltracker.models.entities.viewmodel;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.schooltracker.models.entities.Course;
import com.example.schooltracker.models.entities.CourseRelatedData;
import com.example.schooltracker.models.repository.CourseRepo;
import com.example.schooltracker.utils.CRUD;

import java.util.List;

public class CourseViewModel extends AndroidViewModel implements CRUD<Course> {
    private CourseRepo courseRepo;
    private LiveData<List<Course>> courses;

    public CourseViewModel(@NonNull Application application) {
        super(application);
        courseRepo = new CourseRepo(application);
    }

    public LiveData<List<Course>> getCoursesByTerm(int termId){
        courseRepo.fetchCoursesNoRelations(termId);
        courses = courseRepo.getListOfCourses();
        return courses;
    }

    public LiveData<CourseRelatedData> getAllRelatedDatabyCourse(int courseId){
        return courseRepo.fetchCourseNotes(courseId);
    }

    public Long[] insertCourse(Course c){
        Long[] longs = courseRepo.insertCourse(c);
        if (longs!=null){
            return longs;
        }
        return null;
    }
    public void insertCourse(List<Course> c){
        courseRepo.insertCourse(c);
    }
    public void deleteCourse(Course c){
        courseRepo.deleteCourse(c);
    }
    public void updateCourse(Course c){
        courseRepo.updateCourse(c);
    }


    @Override
    public void insert(Course ob) {
        courseRepo.insertCourse(ob);
    }

    @Override
    public void delete(Course ob) {
        courseRepo.deleteCourse(ob);
    }

    @Override
    public void update(Course ob) {
        courseRepo.updateCourse(ob);
    }
}
