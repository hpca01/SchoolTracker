package com.example.schooltracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.schooltracker.databinding.ActivityNewNoteBinding;
import com.example.schooltracker.models.entities.Note;
import com.example.schooltracker.models.entities.viewmodel.NoteViewModel;
import com.example.schooltracker.ui.NewNoteVM;

public class NewNote extends AppCompatActivity {
    private static final String TAG = "NewNote";

    private NoteViewModel nvm;
    private NewNoteVM newNoteVM;

    private int courseId;

    private int noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_new_note);
        nvm = new NoteViewModel(getApplication());
        ActivityNewNoteBinding newNoteBinding = DataBindingUtil.setContentView(this, R.layout.activity_new_note);
        newNoteVM = new ViewModelProvider(this).get(NewNoteVM.class);
        newNoteBinding.setLifecycleOwner(this);
        newNoteBinding.setNewNoteVM(newNoteVM);
        newNoteBinding.setHandler(this);

        Intent intent = getIntent();
        courseId = intent.getIntExtra("course_id", -1); // > -1 if new
        noteId = intent.getIntExtra("note_id", -1); // > -1 if editing
        if (noteId > -1){
            getNoteById(noteId);
        }
    }

    public void getNoteById(int noteId){
        nvm.getNoteById(noteId).observe(this, new Observer<Note>() {
            @Override
            public void onChanged(Note note) {
                newNoteVM.setNote(note);
            }
        });
    }

    public void saveData(){
        if (newNoteVM.isValid()){
            Note note = newNoteVM.getNote();
            if (noteId > 0){
                //if editing
                nvm.updateNote(note);
                Intent courseDetails = new Intent(this, CourseDetails.class);
                courseDetails.putExtra("course_id", note.getCourseId());
                startActivity(courseDetails);
                finish();
            }else{
                //if new
                note.setCourseId(courseId);
                nvm.insertNote(note);
                Intent courseDetails = new Intent(this, CourseDetails.class);
                courseDetails.putExtra("course_id", courseId);
                startActivity(courseDetails);
                finish();
            }

        }else{
            Toast.makeText(this, "Note needs to be filled out", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent courseDetails = new Intent(this, CourseDetails.class);
        courseDetails.putExtra("course_id", courseId);
        startActivity(courseDetails);
        finish();
    }
}
