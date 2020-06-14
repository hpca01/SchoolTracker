package com.example.schooltracker.models.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.schooltracker.models.entities.DAO.SchoolDao;
import com.example.schooltracker.models.entities.SchoolDatabase;
import com.example.schooltracker.models.entities.Term;
import com.example.schooltracker.models.entities.TermWithCourses;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class TermRepo{

    private SchoolDao schoolDao;
    private LiveData<List<Term>> listOfTerms;

    public TermRepo(Application app) {
        SchoolDatabase db = SchoolDatabase.getDb(app);
        schoolDao = db.schoolDao();
        listOfTerms = schoolDao.getTermsNoCourses();
    }

    public LiveData<List<Term>> getListOfTerms(){
        return listOfTerms;
    }

    public LiveData<List<TermWithCourses>> fetchTermsWithCourses(){
        return schoolDao.loadTerms();
    }

    public void insertTerm(Term t){
        new InsertAsyncTerm(schoolDao).execute(t);
    }

    public void insertTerm(List<Term> t){
        Term[] terms = t.stream().toArray(Term[]::new);
        new InsertAsyncTerm(schoolDao).execute(terms);
    }

    public void updateTerm(Term t){
        new UpdateAsyncTerm(schoolDao).execute(t);
    }

    public void deleteTerm(Term t){
        new DeleteAsyncTerm(schoolDao).execute(t);
    }

    private static class DeleteAsyncTerm extends AsyncTask<Term, Void, Void>{
        private SchoolDao schoolDao;

        public DeleteAsyncTerm(SchoolDao schoolDao) {
            this.schoolDao = schoolDao;
        }

        @Override
        protected Void doInBackground(Term... terms) {
            Arrays.stream(terms).forEach(each->schoolDao.deleteTerm(each));
            return null;
        }
    }

    private static class UpdateAsyncTerm extends AsyncTask<Term, Void, Void>{
        private SchoolDao schoolDao;

        public UpdateAsyncTerm(SchoolDao schoolDao) {
            this.schoolDao = schoolDao;
        }

        @Override
        protected Void doInBackground(Term... terms) {
            Arrays.stream(terms).forEach(each->schoolDao.updateTerm(each));
            return null;
        }
    }

    private static class InsertAsyncTerm extends AsyncTask<Term, Void, Void>{
        private SchoolDao schoolDao;

        public InsertAsyncTerm(SchoolDao schoolDao) {
            this.schoolDao = schoolDao;
        }

        @Override
        protected Void doInBackground(Term... terms) {
            Arrays.stream(terms).forEach(each->schoolDao.insertTerm(each));
            return null;
        }
    }

}
