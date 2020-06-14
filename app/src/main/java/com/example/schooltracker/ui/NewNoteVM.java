package com.example.schooltracker.ui;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.schooltracker.models.entities.Note;

public class NewNoteVM extends ViewModel {
    private Note note = null;

    public MutableLiveData<String> noteText = new MutableLiveData<>();

    public boolean isValid(){
        if(noteText.getValue() == null || noteText.getValue().isEmpty()) return false;
        return true;
    }

    public Note getNote(){
        if(this.note == null){
            note = new Note(noteText.getValue());
        }else{
            this.note.setNoteText(noteText.getValue());
        }
        return note;
    }

    public void setNote(Note note){
        this.note = note;
        this.noteText.setValue(note.getNoteText());
    }

}
