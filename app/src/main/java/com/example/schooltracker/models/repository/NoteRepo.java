package com.example.schooltracker.models.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.schooltracker.models.entities.DAO.SchoolDao;
import com.example.schooltracker.models.entities.Note;
import com.example.schooltracker.models.entities.SchoolDatabase;

import java.util.Arrays;
import java.util.List;

public class NoteRepo {

    private SchoolDao schoolDao;
    private LiveData<List<Note>> notes;

    public NoteRepo(Application app) {
        SchoolDatabase db = SchoolDatabase.getDb(app);
        schoolDao = db.schoolDao();
    }

    public LiveData<List<Note>> getNotes(int courseId) {
        LiveData<List<Note>> notesByCourse = schoolDao.findNotesByCourse(courseId);
        notes = notesByCourse;
        return notes;
    }

    public LiveData<Note> getNoteByNoteId(int noteId){
        return schoolDao.findNoteById(noteId);
    }

    public void insertNote(Note ob){
        new InsertAsyncNote(schoolDao).execute(ob);
    }

    public void updateNote(Note ob){
        new UpdateAsyncNote(schoolDao).execute(ob);
    }

    public void deleteNote(Note ob){
        new DeleteAsyncNote(schoolDao).execute(ob);
    }


    private static class DeleteAsyncNote extends AsyncTask<Note, Void, Void> {
        private SchoolDao schoolDao;

        public DeleteAsyncNote(SchoolDao schoolDao) {
            this.schoolDao = schoolDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            Arrays.stream(notes).forEach(each->schoolDao.deleteNote(each));
            return null;
        }
    }

    private static class UpdateAsyncNote extends AsyncTask<Note, Void, Void>{
        private SchoolDao schoolDao;

        public UpdateAsyncNote(SchoolDao schoolDao) {
            this.schoolDao = schoolDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            Arrays.stream(notes).forEach(each->schoolDao.updateNote(each));
            return null;
        }
    }

    private static class InsertAsyncNote extends AsyncTask<Note, Void, Void>{
        private SchoolDao schoolDao;

        public InsertAsyncNote(SchoolDao schoolDao) {
            this.schoolDao = schoolDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            Arrays.stream(notes).forEach(each->schoolDao.insertNote(each));
            return null;
        }
    }
}
