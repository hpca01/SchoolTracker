package com.example.schooltracker.models.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.schooltracker.models.entities.Course;
import com.example.schooltracker.models.entities.CourseRelatedData;
import com.example.schooltracker.models.entities.DAO.SchoolDao;
import com.example.schooltracker.models.entities.SchoolDatabase;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CourseRepo {

    private SchoolDao schoolDao;
    private LiveData<List<Course>> listOfCourses;

    public CourseRepo(Application app) {
        SchoolDatabase db = SchoolDatabase.getDb(app);
        schoolDao = db.schoolDao();
    }

    public void fetchCoursesNoRelations(int termId){
        listOfCourses = schoolDao.getCoursesById(termId);
    }

    public LiveData<List<Course>> getListOfCourses() {
        return listOfCourses;
    }

    public LiveData<CourseRelatedData> fetchCourseNotes(int courseId){
        return schoolDao.loadCourseWithNotes(courseId);
    }

    public Long[] insertCourse(Course c){
        try {
            return new InsertAsyncCourse(schoolDao).execute(c).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Long[] insertCourse(List<Course> courses){
        try {
            new InsertAsyncCourse(schoolDao).execute(courses.stream().toArray(Course[]::new)).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateCourse(Course c){
        new UpdateAsyncCourse(schoolDao).execute(c);
    }
    public void updateCourse(List<Course> c){
        new UpdateAsyncCourse(schoolDao).execute(c.stream().toArray(Course[]::new));
    }

    public void deleteCourse(Course c){
        new DeleteAsyncCourse(schoolDao).execute(c);
    }

    public void deleteCourse(List<Course> c){
        new DeleteAsyncCourse(schoolDao).execute(c.stream().toArray(Course[]::new));
    }

    private static class DeleteAsyncCourse extends AsyncTask<Course, Void, Void>{
        private SchoolDao schoolDao;

        public DeleteAsyncCourse(SchoolDao schoolDao) {
            this.schoolDao = schoolDao;
        }

        @Override
        protected Void doInBackground(Course... courses) {
            Arrays.stream(courses).forEach(each->schoolDao.deleteCourse(each));
            return null;
        }
    }

    private static class UpdateAsyncCourse extends AsyncTask<Course, Void, Void>{
        private SchoolDao schoolDao;

        public UpdateAsyncCourse(SchoolDao schoolDao) {
            this.schoolDao = schoolDao;
        }

        @Override
        protected Void doInBackground(Course... courses) {
            Arrays.stream(courses).forEach(each->schoolDao.updateCourse(each));
            return null;
        }
    }

    private static class InsertAsyncCourse extends AsyncTask<Course, Long[], Long[]>{
        private SchoolDao schoolDao;

        public InsertAsyncCourse(SchoolDao schoolDao) {
            this.schoolDao = schoolDao;
        }

        @Override
        protected Long[] doInBackground(Course... courses) {
            return Arrays.stream(courses).map(each->schoolDao.insertCourse(each)).toArray(Long[]::new);
        }
    }
}
