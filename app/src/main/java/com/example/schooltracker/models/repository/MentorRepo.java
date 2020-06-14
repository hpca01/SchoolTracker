package com.example.schooltracker.models.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.schooltracker.models.entities.DAO.SchoolDao;
import com.example.schooltracker.models.entities.Mentor;
import com.example.schooltracker.models.entities.SchoolDatabase;

import java.util.Arrays;
import java.util.List;

public class MentorRepo {
    private SchoolDao schoolDao;

    private LiveData<List<Mentor>> listOfMentors;

    public MentorRepo(Application app) {
        SchoolDatabase db = SchoolDatabase.getDb(app);
        schoolDao = db.schoolDao();
    }

    public LiveData<List<Mentor>> getListOfMentors(){
        listOfMentors = schoolDao.getAllMentors();
        return listOfMentors;
    }

    public LiveData<List<Mentor>> getMentorsById(List<Integer> mentorIds){
        return schoolDao.getMentorsById(mentorIds);
    }


    public void insertMentor(Mentor m){
        new InsertAsyncMentor(schoolDao).execute(m);
    }
    public void updateMentor(Mentor m){
        new UpdateAsyncMentor(schoolDao).execute(m);
    }
    public void deleteMentor(Mentor m){
        new DeleteAsyncMentor(schoolDao).execute(m);
    }



    private static class DeleteAsyncMentor extends AsyncTask<Mentor, Void, Void> {
        private SchoolDao schoolDao;

        public DeleteAsyncMentor(SchoolDao schoolDao) {
            this.schoolDao = schoolDao;
        }

        @Override
        protected Void doInBackground(Mentor... mentors) {
            Arrays.stream(mentors).forEach(each->schoolDao.deleteMentor(each));
            return null;
        }
    }

    private static class UpdateAsyncMentor extends AsyncTask<Mentor, Void, Void>{
        private SchoolDao schoolDao;

        public UpdateAsyncMentor(SchoolDao schoolDao) {
            this.schoolDao = schoolDao;
        }

        @Override
        protected Void doInBackground(Mentor... mentors) {
            Arrays.stream(mentors).forEach(each->schoolDao.updateMentor(each));
            return null;
        }
    }

    private static class InsertAsyncMentor extends AsyncTask<Mentor, Void, Void>{
        private SchoolDao schoolDao;

        public InsertAsyncMentor(SchoolDao schoolDao) {
            this.schoolDao = schoolDao;
        }

        @Override
        protected Void doInBackground(Mentor... mentors) {
            Arrays.stream(mentors).forEach(each->schoolDao.insertMentor(each));
            return null;
        }
    }

}
