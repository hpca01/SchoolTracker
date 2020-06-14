package com.example.schooltracker;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.example.schooltracker.models.entities.Course;
import com.example.schooltracker.models.entities.Mentor;
import com.example.schooltracker.models.entities.viewmodel.CourseViewModel;
import com.example.schooltracker.models.entities.viewmodel.MentorViewModel;
import com.example.schooltracker.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditCourse extends AppCompatActivity {
    private static final String TAG = "EditCourse";
    @BindView(R.id.courseTitle)
    public EditText courseTitle;

    @BindView(R.id.courseStart)
    public EditText startDate;

    @BindView(R.id.courseEnd)
    public EditText endDate;

    @BindView(R.id.courseAlert)
    public Switch alert;

    @BindView(R.id.statusRadioGroup)
    public RadioGroup status;

    @BindView(R.id.saveCourse)
    public Button saveBtn;

    @BindView(R.id.courseDropped)
    public RadioButton dropped;

    Course.STATUS currentCourseStatus = null;
    private int termId;
    private int courseId;

    Calendar startCal = Calendar.getInstance();
    Calendar endCal = Calendar.getInstance();

    CourseViewModel cvm = new CourseViewModel(getApplication());
    private List<Course> coursesList = null;
    private Course currentCourse = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_course);
        ButterKnife.bind(this);
        Intent intent = getIntent();

        termId = intent.getIntExtra("term_id", 0); //important for returning back to term details
        courseId = intent.getIntExtra("course_id", 0); //important for finding the right course to edit
        getCourses();

    }


    public void getCourses(){
        //setup form to represent course state
        cvm.getCoursesByTerm(termId).observe(this, new Observer<List<Course>>() {
            @Override
            public void onChanged(List<Course> courses) {
                coursesList = courses;
                setUpForm();
            }
        });
    }

    private void setUpForm() {
        Optional<Course> first = coursesList.stream().filter(each -> each.getId() == courseId).findFirst();
        if (first.isPresent()){
            currentCourse = first.get();
            courseTitle.setText(currentCourse.getTitle());
            startDate.setText(Utils.getStringFromDate(currentCourse.getStart()));
            endDate.setText(Utils.getStringFromDate(currentCourse.getEnd()));
            startCal.setTime(currentCourse.getStart());
            endCal.setTime(currentCourse.getEnd());
            currentCourseStatus = currentCourse.getStatus();
            onDateSet();
            statusSetup();
        }
    }

    public void onDateSet() {
        DatePickerDialog.OnDateSetListener startDatePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                startCal.set(Calendar.MONTH, month);
                startCal.set(Calendar.YEAR, year);
                startCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateEditText(startDate, startCal);
            }
        };

        DatePickerDialog.OnDateSetListener endDatePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                endCal.set(Calendar.MONTH, month);
                endCal.set(Calendar.YEAR, year);
                endCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateEditText(endDate, endCal);
            }
        };
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditCourse.this, startDatePicker, startCal.get(Calendar.YEAR), startCal.get(Calendar.MONTH), startCal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditCourse.this, endDatePicker, endCal.get(Calendar.YEAR), endCal.get(Calendar.MONTH), endCal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    public void updateEditText(EditText text, Calendar cal){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/YYYY", Locale.US);
        text.setText(sdf.format(cal.getTime()));
        text.setError(null);
    }

    private void statusSetup(){
        if (currentCourseStatus!=null){
            switch (currentCourseStatus){
                case PLANNED:
                    RadioButton planned = (RadioButton) findViewById(R.id.coursePlanned);
                    planned.setChecked(true);
                    break;
                case IN_PROGRESS:
                    RadioButton inProgress = (RadioButton) findViewById(R.id.courseInProgress);
                    inProgress.setChecked(true);
                    break;
                case COMPLETED:
                    RadioButton completed = (RadioButton) findViewById(R.id.courseCompleted);
                    completed.setChecked(true);
                    break;
                case DROPPED:
                    RadioButton dropped = (RadioButton) findViewById(R.id.courseDropped);
                    dropped.setChecked(true);
                    break;
            }
        }
        status.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                dropped.setError(null);
                switch(checkedId){
                    case R.id.coursePlanned:
                        currentCourseStatus = Course.STATUS.PLANNED;
                        break;
                    case R.id.courseInProgress:
                        currentCourseStatus = Course.STATUS.IN_PROGRESS;
                        break;
                    case R.id.courseCompleted:
                        currentCourseStatus = Course.STATUS.COMPLETED;
                        break;
                    case R.id.courseDropped:
                        currentCourseStatus = Course.STATUS.DROPPED;
                        break;
                }
            }
        });

    }

    private boolean validateForm(){
        ArrayList<EditText> requiredFields = new ArrayList<EditText>() {
            {
                add(courseTitle);
                add(startDate);
                add(endDate);
            }
        };
        Stream<EditText> validationErrors = requiredFields.stream().filter(each -> each.getText().toString().isEmpty());
        if (validationErrors.count() > 0) {
            validationErrors = requiredFields.stream().filter(each -> each.getText().toString().isEmpty());
            validationErrors.forEach(each -> each.setError(each.getHint() + " is required"));
            return false;
        }
        if(currentCourseStatus == null){
            dropped.setError("Course status required");
            return false;
        }
        return true;
    }

    @OnClick(R.id.saveCourse)
    public void saveForm(){
        if(validateForm()){
            currentCourse.setTitle(Utils.getString(courseTitle));
            currentCourse.setStart(Utils.getDateString(startDate));
            currentCourse.setEnd(Utils.getDateString(endDate));
            currentCourse.setStatus(currentCourseStatus);
            currentCourse.setAlert(alert.isChecked());
            cvm.updateCourse(currentCourse); //need to update course info
            if(currentCourse.isAlert()){
                AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent startAlert = new Intent(this, GoalAlertReceiver.class);
                startAlert.putExtra("ALERT_TYPE", GoalAlertReceiver.COURSE);
                startAlert.putExtra("course_name", currentCourse.getTitle());
                startAlert.putExtra("course_start", true);
                startCal.set(Calendar.HOUR_OF_DAY, 0);
                startCal.set(Calendar.MINUTE, 0);
                PendingIntent startPendingIntent = PendingIntent.getBroadcast(this, 4, startAlert, 0);
                manager.set(AlarmManager.RTC_WAKEUP, startCal.getTimeInMillis(), startPendingIntent);

                Intent endAlert = new Intent(this, GoalAlertReceiver.class);
                endAlert.putExtra("ALERT_TYPE", GoalAlertReceiver.COURSE);
                endAlert.putExtra("course_name", currentCourse.getTitle());
                endAlert.putExtra("course_start", false);
                endCal.set(Calendar.HOUR_OF_DAY, 0);
                endCal.set(Calendar.MINUTE, 0);
                PendingIntent endPendingIntent = PendingIntent.getBroadcast(this, 5, endAlert, 0);
                manager.set(AlarmManager.RTC_WAKEUP, endCal.getTimeInMillis(), endPendingIntent);
            }

            Intent rtnToCourses = new Intent(this, TermDetails.class);
            rtnToCourses.putExtra("term_id", termId);
            startActivity(rtnToCourses);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        Intent rtnToCourses = new Intent(this, TermDetails.class);
        rtnToCourses.putExtra("term_id", termId);
        startActivity(rtnToCourses);
        finish();
    }
}
