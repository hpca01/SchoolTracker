package com.example.schooltracker;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.example.schooltracker.models.entities.Term;
import com.example.schooltracker.models.entities.TermWithCourses;
import com.example.schooltracker.models.entities.viewmodel.TermViewModel;
import com.example.schooltracker.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;
import java.util.stream.Stream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditTerm extends AppCompatActivity {
    private static final String TAG = "EditTerm";

    TermViewModel tvm = new TermViewModel(getApplication());
    private List<TermWithCourses> terms = new ArrayList<>();
    private TermWithCourses activeTerm = null;


    @BindView(R.id.termTitle)
    public EditText title;

    @BindView(R.id.startDate)
    public EditText startDate;

    @BindView(R.id.endDate)
    public EditText endDate;

    @BindView(R.id.alert)
    public Switch alert;

    @BindView(R.id.add_term)
    public Button addTerm;

    Calendar startCal = Calendar.getInstance();
    Calendar endCal = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_term);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        int termId = intent.getIntExtra("term_id", 0);
        getTermData(termId);

    }

    void setUpForm(){
        Term t = activeTerm.term;
        title.setText(t.getTitle());
        startDate.setText(Utils.getStringFromDate(t.getStart()));
        endDate.setText(Utils.getStringFromDate(t.getEnd()));
        alert.setChecked(t.isAlert());
        startCal.setTime(t.getStart());
        endCal.setTime(t.getEnd());
        onDateSet();
    }

    void getTermData(int termId){
        tvm.getTermWithCourse().observe(this, new Observer<List<TermWithCourses>>() {
            @Override
            public void onChanged(List<TermWithCourses> termWithCourses) {
                terms.addAll(termWithCourses);
                Supplier<Stream<TermWithCourses>> termsStream = () -> termWithCourses.stream().filter(each -> each.term.getId() == termId);
                if(termsStream.get().count()>0){
                    activeTerm = termsStream.get().findFirst().get();
                    setUpForm();
                }
            }
        });
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
                new DatePickerDialog(EditTerm.this, startDatePicker, startCal.get(Calendar.YEAR), startCal.get(Calendar.MONTH), startCal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditTerm.this, endDatePicker, endCal.get(Calendar.YEAR), endCal.get(Calendar.MONTH), endCal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    public void updateEditText(EditText text, Calendar cal){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/YYYY", Locale.US);
        text.setText(sdf.format(cal.getTime()));
        text.setError(null);
    }

    public boolean isValidForm() {
        ArrayList<EditText> requiredFields = new ArrayList<EditText>() {
            {
                add(title);
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

        return true;
    }

    @OnClick(R.id.add_term)
    public void submit(){
        if(isValidForm()){
            TermViewModel tvm = new TermViewModel(getApplication());
            Term t = activeTerm.term;
            t.setTitle(Utils.getString(title));
            t.setStart(Utils.getDateString(startDate));
            t.setEnd(Utils.getDateString(endDate));
            t.setAlert(alert.isChecked());
            if(t.isAlert()){
                AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(this, GoalAlertReceiver.class);
                intent.putExtra("ALERT_TYPE", GoalAlertReceiver.TERM);
                //put term data
                intent.putExtra("term_name", t.getTitle());
                intent.putExtra("term_start", Utils.getStringFromDate(t.getStart()));
                startCal.set(Calendar.HOUR_OF_DAY, 0);
                startCal.set(Calendar.MINUTE, 0);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this,1, intent, 0);
                assert manager != null;
                manager.setExact(AlarmManager.RTC_WAKEUP, startCal.getTimeInMillis(), pendingIntent);
            }
            tvm.updateTerm(t);
            Intent intent = new Intent(this, TermsView.class);
            startActivity(intent);
            finish();
        }
    }

}
