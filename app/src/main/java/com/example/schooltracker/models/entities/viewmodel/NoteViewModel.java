package com.example.schooltracker.models.entities.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.schooltracker.models.entities.Note;
import com.example.schooltracker.models.repository.NoteRepo;
import com.example.schooltracker.utils.CRUD;

import java.util.List;

public class NoteViewModel extends AndroidViewModel implements CRUD<Note> {
    private NoteRepo noteRepo;
    private LiveData<List<Note>> notes;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        noteRepo = new NoteRepo(application);
    }

    public LiveData<List<Note>> getNotesByCourse(int courseId){
        notes = noteRepo.getNotes(courseId);
        return notes;
    }

    public LiveData<Note> getNoteById(int noteId){
        return noteRepo.getNoteByNoteId(noteId);
    }

    public void insertNote(Note ob){
        noteRepo.insertNote(ob);
    }

    public void updateNote(Note ob){
        noteRepo.updateNote(ob);
    }

    public void deleteNote(Note ob){
        noteRepo.deleteNote(ob);
    }

    @Override
    public void insert(Note ob) {
        noteRepo.insertNote(ob);
    }

    @Override
    public void delete(Note ob) {
        noteRepo.deleteNote(ob);
    }

    @Override
    public void update(Note ob) {
        noteRepo.updateNote(ob);
    }
}
