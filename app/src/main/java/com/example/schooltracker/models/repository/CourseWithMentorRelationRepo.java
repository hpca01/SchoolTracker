package com.example.schooltracker.models.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;


import com.example.schooltracker.models.entities.CourseWithMentor;
import com.example.schooltracker.models.entities.DAO.SchoolDao;
import com.example.schooltracker.models.entities.SchoolDatabase;

import java.util.Arrays;
import java.util.List;

public class CourseWithMentorRelationRepo {

    private SchoolDao schoolDao;

    private LiveData<List<CourseWithMentor>> assessmentLiveData;

    public CourseWithMentorRelationRepo(Application app) {
        SchoolDatabase db = SchoolDatabase.getDb(app);
        schoolDao = db.schoolDao();
    }

    public LiveData<List<CourseWithMentor>> getAllCoursesWithMentors(){
        return schoolDao.allMentorCourseData();
    }

    public void insertCourseWithMentor(CourseWithMentor c){
        new InsertAsyncCourseWithMentor(schoolDao).execute(c);
    }

    public void updateCourseWithMentor(CourseWithMentor c){
        new UpdateAsyncCourseWithMentor(schoolDao).execute(c);
    }

    public void deleteCourseWithMentor(CourseWithMentor c){
        new DeleteAsyncCourseWithMentor(schoolDao).execute(c);
    }

    private static class DeleteAsyncCourseWithMentor extends AsyncTask<CourseWithMentor, Void, Void> {
        private SchoolDao schoolDao;

        public DeleteAsyncCourseWithMentor(SchoolDao schoolDao) {
            this.schoolDao = schoolDao;
        }

        @Override
        protected Void doInBackground(CourseWithMentor... courseWithMentors) {
            Arrays.stream(courseWithMentors).forEach(each->schoolDao.deleteCourseWithMentor(each));
            return null;
        }
    }

    private static class UpdateAsyncCourseWithMentor extends AsyncTask<CourseWithMentor, Void, Void>{
        private SchoolDao schoolDao;

        public UpdateAsyncCourseWithMentor(SchoolDao schoolDao) {
            this.schoolDao = schoolDao;
        }

        @Override
        protected Void doInBackground(CourseWithMentor... courseWithMentors) {
            Arrays.stream(courseWithMentors).forEach(each->schoolDao.updateCourseWithMentor(each));
            return null;
        }
    }

    private static class InsertAsyncCourseWithMentor extends AsyncTask<CourseWithMentor, Void, Void>{
        private SchoolDao schoolDao;

        public InsertAsyncCourseWithMentor(SchoolDao schoolDao) {
            this.schoolDao = schoolDao;
        }

        @Override
        protected Void doInBackground(CourseWithMentor... courseWithMentors) {
            Arrays.stream(courseWithMentors).forEach(each->schoolDao.insertCourseWithMentor(each));
            return null;
        }
    }

}
