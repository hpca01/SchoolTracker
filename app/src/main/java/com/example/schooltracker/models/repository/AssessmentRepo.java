package com.example.schooltracker.models.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.schooltracker.models.entities.Assessment;
import com.example.schooltracker.models.entities.DAO.SchoolDao;
import com.example.schooltracker.models.entities.SchoolDatabase;

import java.util.Arrays;
import java.util.List;

public class AssessmentRepo {

    private SchoolDao schoolDao;

    private LiveData<List<Assessment>> assessmentLiveData;

    public AssessmentRepo(Application app) {
        SchoolDatabase db = SchoolDatabase.getDb(app);
        schoolDao = db.schoolDao();
    }

    public LiveData<List<Assessment>> getAssessmentByCourse(int id) {
        assessmentLiveData = schoolDao.findAssessmentByCourse(id);
        return assessmentLiveData;
    }

    public LiveData<Assessment> getAssessmentById(int id){
        return schoolDao.getAssessmentById(id);
    }

    public void insertAssessment(Assessment ob){
        new InsertAsyncAssessment(schoolDao).execute(ob);
    }

    public void updateAssessment(Assessment ob){
        new UpdateAsyncAssessment(schoolDao).execute(ob);
    }

    public void deleteAssessment(Assessment ob){
        new DeleteAsyncAssessment(schoolDao).execute(ob);
    }

    private static class DeleteAsyncAssessment extends AsyncTask<Assessment, Void, Void> {
        private SchoolDao schoolDao;

        public DeleteAsyncAssessment(SchoolDao schoolDao) {
            this.schoolDao = schoolDao;
        }

        @Override
        protected Void doInBackground(Assessment... assessments) {
            Arrays.stream(assessments).forEach(each->schoolDao.deleteAssessment(each));
            return null;
        }
    }

    private static class UpdateAsyncAssessment extends AsyncTask<Assessment, Void, Void>{
        private SchoolDao schoolDao;

        public UpdateAsyncAssessment(SchoolDao schoolDao) {
            this.schoolDao = schoolDao;
        }

        @Override
        protected Void doInBackground(Assessment... assessments) {
            Arrays.stream(assessments).forEach(each->schoolDao.updateAssessment(each));
            return null;
        }
    }

    private static class InsertAsyncAssessment extends AsyncTask<Assessment, Void, Void>{
        private SchoolDao schoolDao;

        public InsertAsyncAssessment(SchoolDao schoolDao) {
            this.schoolDao = schoolDao;
        }

        @Override
        protected Void doInBackground(Assessment... assessments) {
            Arrays.stream(assessments).forEach(each->schoolDao.insertAssessment(each));
            return null;
        }
    }
}
