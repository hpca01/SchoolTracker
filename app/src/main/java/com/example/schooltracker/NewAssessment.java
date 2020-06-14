package com.example.schooltracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.schooltracker.databinding.ActivityNewAssessmentBinding;
import com.example.schooltracker.databinding.ActivityNewMentorBinding;
import com.example.schooltracker.models.entities.Assessment;
import com.example.schooltracker.models.entities.viewmodel.AssessmentViewModel;
import com.example.schooltracker.models.entities.viewmodel.CourseViewModel;
import com.example.schooltracker.ui.NewAssessmentVM;
import com.example.schooltracker.utils.Utils;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewAssessment extends AppCompatActivity {

    private NewAssessmentVM newAssessmentVM;
    private AssessmentViewModel avm;

    private CourseViewModel cvm;

    private static final String TAG = "NewAssessment";

    private int assessmentId;
    private int courseId;

    @BindView(R.id.start_date)
    public EditText startDate;

    @BindView(R.id.end_date)
    public EditText endDate;

    @BindView(R.id.typeRadioGroup) public RadioGroup radiogroup;

    ActivityNewAssessmentBinding newAssessmentBinding;


    Calendar startCalDate = Calendar.getInstance();
    Calendar endCalDate = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_new_assessment);

        avm = new AssessmentViewModel(getApplication());
        cvm = new CourseViewModel(getApplication());


        newAssessmentBinding = DataBindingUtil.setContentView(this, R.layout.activity_new_assessment);
        newAssessmentVM = new ViewModelProvider(this).get(NewAssessmentVM.class);
        newAssessmentBinding.setLifecycleOwner(this);
        newAssessmentBinding.setHandler(this);
        newAssessmentBinding.setAssessmentVM(newAssessmentVM);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        assessmentId = intent.getIntExtra("assessment_id", -1); //needed for edit assessments
        courseId = intent.getIntExtra("course_id", -1); //needed for new assessments

        if (assessmentId > 0){
            //this means it's an edit
            getAssessmentById(assessmentId);
        }
        onDateSet();
        setupRadioGroupListener();
    }

    private void setupRadioGroupListener() {
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.performanceType:
                        newAssessmentVM.setPerformanceAssessment(true);
                        break;
                    case R.id.objectiveType:
                        newAssessmentVM.setPerformanceAssessment(false);
                        break;
                }
            }
        });
    }

    public void onDateSet() {
        DatePickerDialog.OnDateSetListener startDatePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                startCalDate.set(Calendar.MONTH, month);
                startCalDate.set(Calendar.YEAR, year);
                startCalDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                newAssessmentVM.setStartDate(startCalDate);
            }
        };

        DatePickerDialog.OnDateSetListener endDatePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                endCalDate.set(Calendar.MONTH, month);
                endCalDate.set(Calendar.YEAR, year);
                endCalDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                newAssessmentVM.setEndDate(endCalDate);
            }
        };
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(NewAssessment.this, startDatePicker, startCalDate.get(Calendar.YEAR), startCalDate.get(Calendar.MONTH), startCalDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(NewAssessment.this, endDatePicker, endCalDate.get(Calendar.YEAR), endCalDate.get(Calendar.MONTH), endCalDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    //edit
    private void getAssessmentById(int assessmentId){
        avm.getAssessmentById(assessmentId).observe(this, new Observer<Assessment>() {
            @Override
            public void onChanged(Assessment assessment) {
                newAssessmentVM.setAssessment(assessment);
                startCalDate.setTime(assessment.getStart());
                endCalDate.setTime(assessment.getEnd());
            }
        });
    }

    //called on save
    public void saveAssessment(){
        if(newAssessmentVM.isValid()){
            Assessment a = newAssessmentVM.getAssessment();
            if(assessmentId == -1){
                //for new ones only
                a.setCourseId(courseId);
                avm.insertAssessment(a);
                Intent intent = new Intent(this, CourseDetails.class);
                intent.putExtra("course_id", courseId);
                startActivity(intent);
                finish();
            }else{
                avm.updateAssessment(a);
                Intent intent = new Intent(this, CourseDetails.class);
                intent.putExtra("course_id", a.getCourseId());
                startActivity(intent);
                finish();
            }
            if (a.isAlert()){
                AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent startAlert = new Intent(this, GoalAlertReceiver.class);
                startAlert.putExtra("ALERT_TYPE", GoalAlertReceiver.ASSESSMENT);
                startAlert.putExtra("assessment_name", a.getTitle());
                startAlert.putExtra("assessment_type", a.getType().toString());
                startAlert.putExtra("assessment_start", true);
                startCalDate.set(Calendar.HOUR_OF_DAY, 0);
                startCalDate.set(Calendar.MINUTE, 0);
                PendingIntent startPendingIntent = PendingIntent.getBroadcast(this, 6, startAlert, 0);
                manager.set(AlarmManager.RTC_WAKEUP, startCalDate.getTimeInMillis(), startPendingIntent);

                Intent endAlert = new Intent(this, GoalAlertReceiver.class);
                endAlert.putExtra("ALERT_TYPE", GoalAlertReceiver.ASSESSMENT);
                endAlert.putExtra("assessment_name", a.getTitle());
                endAlert.putExtra("assessment_type", a.getType().toString());
                endAlert.putExtra("assessment_start", false);
                endCalDate.set(Calendar.HOUR_OF_DAY, 0);
                endCalDate.set(Calendar.MINUTE, 0);
                PendingIntent endPendingIntent = PendingIntent.getBroadcast(this, 7, endAlert, 0);
                manager.set(AlarmManager.RTC_WAKEUP, endCalDate.getTimeInMillis(), endPendingIntent);
            }
        }else{
            Toast.makeText(this, "Please fill out start date, end date, and type of assessment", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, CourseDetails.class);
        intent.putExtra("course_id", courseId);
        startActivity(intent);
        finish();
    }
}
