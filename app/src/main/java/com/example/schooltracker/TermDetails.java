package com.example.schooltracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schooltracker.models.entities.Course;
import com.example.schooltracker.models.entities.TermWithCourses;
import com.example.schooltracker.models.entities.viewmodel.CourseViewModel;
import com.example.schooltracker.models.entities.viewmodel.TermViewModel;
import com.example.schooltracker.ui.CoursesAdapter;
import com.example.schooltracker.utils.Clickable;
import com.example.schooltracker.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TermDetails extends AppCompatActivity implements Clickable {

    private static final String TAG = "TermDetails";

    private List<TermWithCourses> terms = new ArrayList<>();
    private TermWithCourses activeTerm = null;

    @BindView(R.id.term_detail_title)
    public TextView termTitle;

    @BindView(R.id.term_detail_start_date)
    public TextView startDate;

    @BindView(R.id.term_detail_end_date)
    public TextView endDate;

    @BindView(R.id.term_detail_no_course_tv)
    public TextView noCourseText;

    @BindView(R.id.term_detail_alert)
    public Switch alert;

    @BindView(R.id.term_detail_course_recycler)
    public RecyclerView coursesRecycler;

    private TermViewModel tvm;
    private int termId;

    private CourseViewModel cvm;

    @BindView(R.id.toolbarLayout) public Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_details);
        ButterKnife.bind(this);
        tvm  = new TermViewModel(getApplication());
        cvm = new CourseViewModel(getApplication());

        Intent intent = getIntent();
        termId = intent.getIntExtra("term_id", 0);
        getTermData(termId);
        setupToolBar("Term Details");

    }

    private void setupToolBar(String title){
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    void setupCourseRecycler(){
        CoursesAdapter coursesAdapter = new CoursesAdapter(getApplicationContext(), this);
        coursesRecycler.setAdapter(coursesAdapter);
        coursesRecycler.setLayoutManager(new LinearLayoutManager(this));
        coursesAdapter.setCourses(activeTerm.associatedCourses);
        ItemTouchHelper itemTouchHelper = Utils.makeSwipeHelper(activeTerm.associatedCourses, cvm, coursesRecycler);
        itemTouchHelper.attachToRecyclerView(coursesRecycler);
    }

    void setUpTermDetails(){
        if (activeTerm != null){
            termTitle.setText(activeTerm.term.getTitle());
            startDate.setText("Start Date "+Utils.getStringFromDate(activeTerm.term.getStart()));
            endDate.setText("End Date "+Utils.getStringFromDate(activeTerm.term.getEnd()));
            alert.setChecked(activeTerm.term.isAlert());
            alert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    activeTerm.term.setAlert(isChecked);
                    tvm.updateTerm(activeTerm.term);
                    Toast.makeText(TermDetails.this, "Note, this will not turn off the alert set by the OS.", Toast.LENGTH_SHORT).show();
                }
            });
            setupCourseRecycler();
            if (activeTerm.associatedCourses.size() == 0){
                noCourseText.setVisibility(View.VISIBLE);
                coursesRecycler.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.term_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(this, TermsView.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.add_course:
                Intent addCourseIntent = new Intent(this, NewCourse.class);
                addCourseIntent.putExtra("term_id", termId);
                startActivity(addCourseIntent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void getTermData(int termId){
        tvm.getTermWithCourse().observe(this, new Observer<List<TermWithCourses>>() {
            @Override
            public void onChanged(List<TermWithCourses> termWithCourses) {
                terms.addAll(termWithCourses);
                Supplier<Stream<TermWithCourses>> termsStream = () -> termWithCourses.stream().filter(each -> each.term.getId() == termId);
                if(termsStream.get().count()>0){
                    activeTerm = termsStream.get().findFirst().get();
                    setUpTermDetails();
                }
            }
        });
    }

    @Override
    public void clicked(int position) {
        //clicked course to course detail view
        Log.d(TAG, "clicked: "+position);
        Course course = activeTerm.associatedCourses.get(position);
        Intent courseDetail = new Intent(this, CourseDetails.class);
        courseDetail.putExtra("course_id", course.getId());
        courseDetail.putExtra("term_id", termId);
        startActivity(courseDetail);
        finish();
    }

    @Override
    public void longClick(int position) {
        //clicked go to course edit view
        Log.d(TAG, "longClick: "+position);
        Course course = activeTerm.associatedCourses.get(position);
        Intent editCourse = new Intent(this, EditCourse.class);
        editCourse.putExtra("term_id", termId);
        editCourse.putExtra("course_id", course.getId());
        startActivity(editCourse);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent termView = new Intent(this, TermsView.class);
        startActivity(termView);
        finish();
    }
}
