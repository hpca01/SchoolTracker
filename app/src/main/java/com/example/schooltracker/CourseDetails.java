package com.example.schooltracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schooltracker.models.entities.Assessment;
import com.example.schooltracker.models.entities.Course;
import com.example.schooltracker.models.entities.CourseRelatedData;
import com.example.schooltracker.models.entities.Mentor;
import com.example.schooltracker.models.entities.Note;
import com.example.schooltracker.models.entities.Term;
import com.example.schooltracker.models.entities.viewmodel.AssessmentViewModel;
import com.example.schooltracker.models.entities.viewmodel.CourseViewModel;
import com.example.schooltracker.models.entities.viewmodel.NoteViewModel;
import com.example.schooltracker.models.entities.viewmodel.TermViewModel;
import com.example.schooltracker.ui.AssessmentAdapter;
import com.example.schooltracker.ui.NotesAdapter;
import com.example.schooltracker.utils.CombinedRecyclerView;
import com.example.schooltracker.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CourseDetails extends AppCompatActivity implements CombinedRecyclerView {

    private static final String TAG = "CourseDetails";

    @BindView(R.id.courseTitle)
    public TextView courseTitle;

    @BindView(R.id.courseStartDate)
    public TextView startDate;

    @BindView(R.id.courseEndDate)
    public TextView endDate;

    @BindView(R.id.notes_recycler)
    public RecyclerView notesListView;

    @BindView(R.id.assessments_recycler)
    public RecyclerView assessmentRecycler;

    CourseViewModel cvm = new CourseViewModel(getApplication());

    TermViewModel tvm;
    NoteViewModel nvm;
    AssessmentViewModel avm;

    CourseRelatedData courseData = null;

    int courseId;
    int termId;

    @BindView(R.id.toolbarLayout) public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);
        ButterKnife.bind(this);
        tvm = new TermViewModel(getApplication());
        nvm = new NoteViewModel(getApplication());
        avm = new AssessmentViewModel(getApplication());

        Intent intent = getIntent();
        courseId = intent.getIntExtra("course_id", 0);
        termId = intent.getIntExtra("term_id", -1);
        getCourseData(courseId);
        setupToolBar("Course Details");
    }

    private void setupToolBar(String title){
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void getCourseData(int courseId) {
        if (courseId > 0) {
            cvm.getAllRelatedDatabyCourse(courseId).observe(this, new Observer<CourseRelatedData>() {
                @Override
                public void onChanged(CourseRelatedData courseRelatedData) {
                    courseData = courseRelatedData;
                    setUpCourseData();
                }
            });
        }
    }

    public void setUpCourseData() {
        if (courseData != null) {
            CourseRelatedData courseRelatedData = courseData;
            Course c = courseRelatedData.course;
            courseTitle.setText(c.getTitle());
            startDate.setText(Utils.getStringFromDate(c.getStart()));
            endDate.setText(Utils.getStringFromDate(c.getEnd()));
            setupMentorData();
        }
    }

    public void setupMentorData() {
        if (courseData != null) {
            CourseRelatedData courseRelatedData = courseData;
            //TODO - setup mentors view
            setupAssessmentData();
        }
    }

    public void setupAssessmentData() {
        if (courseData != null) {
            CourseRelatedData courseRelatedData = courseData;
            AssessmentAdapter adapter = new AssessmentAdapter(getApplicationContext(), this);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            Utils.addDividers(assessmentRecycler, linearLayoutManager);
            assessmentRecycler.setLayoutManager(linearLayoutManager);
            assessmentRecycler.setAdapter(adapter);
            if (courseRelatedData.assessments != null) {
                adapter.setAssessmentList(courseRelatedData.assessments);
            }
            ItemTouchHelper itemTouchHelper = Utils.makeSwipeHelper(courseRelatedData.assessments, avm, assessmentRecycler);
            itemTouchHelper.attachToRecyclerView(assessmentRecycler);
            setupNotesData();
        }
    }

    public void setupNotesData() {
        if (courseData != null) {
            CourseRelatedData courseRelatedData = courseData;
            NotesAdapter adapter = new NotesAdapter(getApplicationContext(), this);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            Utils.addDividers(notesListView, linearLayoutManager);
            notesListView.setLayoutManager(linearLayoutManager);
            notesListView.setAdapter(adapter);
            if (courseRelatedData.notes != null) {
                adapter.setNotes(courseRelatedData.notes);
            }
            ItemTouchHelper itemTouchHelper = Utils.makeSwipeHelper(courseRelatedData.notes, nvm, notesListView);
            itemTouchHelper.attachToRecyclerView(notesListView);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.course_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share_course:
                Intent emailIntent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"recipient@gmail.com"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, courseData.course.getTitle());
                List<String> collectionOfNotes = courseData.notes.stream().map(note -> note.getNoteText() + "\n").collect(Collectors.toList());
                String notesText = collectionOfNotes.stream().reduce("", (note1, note2) -> note1 + note2);
                emailIntent.putExtra(Intent.EXTRA_TEXT, notesText);
                startActivity(Intent.createChooser(emailIntent, "Send Course info via email to..."));
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.add_assessment:
                Intent newAssessment = new Intent(this, NewAssessment.class);
                newAssessment.putExtra("course_id", courseId);
                startActivity(newAssessment);
                finish();
                return true;
            case R.id.add_note:
                Intent newNote = new Intent(this, NewNote.class);
                newNote.putExtra("course_id", courseId);
                startActivity(newNote);
                finish();
                return true;
            case R.id.add_mentor:
                Intent addMentor = new Intent(this, CourseAddMentor.class);
                addMentor.putExtra("course_id", courseId);
                startActivity(addMentor);
                finish();
                return true;
        }

        return true;
    }

    @Override
    public void notesClick(int position) {
        if(courseData.notes !=null) {
            List<Note> notes = courseData.notes;
            Note note = notes.get(position);
            Intent editNote = new Intent(this, NewNote.class);
            editNote.putExtra("note_id", note.getId());
            startActivity(editNote);
            finish();
        }
    }

    @Override
    public void notesLongClick(int position) {
        Toast.makeText(this, "Action not allowed for this note", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void assessmentClick(int position) {
        if(courseData.assessments!=null){
            List<Assessment> assessments = courseData.assessments;
            Assessment assessment = assessments.get(position);
            Intent editAssessment = new Intent(this, NewAssessment.class);
            editAssessment.putExtra("course_id", assessment.getCourseId());
            editAssessment.putExtra("assessment_id", assessment.getId());
            startActivity(editAssessment);
            finish();
        }

    }

    @Override
    public void assessmentLongClick(int position) {
        Toast.makeText(this, "Action not allowed for this assessment", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (termId>-1){
            Intent termDetails = new Intent(this, TermDetails.class);
            termDetails.putExtra("term_id", termId);
            startActivity(termDetails);
            finish();
        }else{
            Intent termView = new Intent(this, TermsView.class);
            startActivity(termView);
            finish();
        }
    }
}
