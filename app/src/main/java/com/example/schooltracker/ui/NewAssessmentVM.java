package com.example.schooltracker.ui;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.schooltracker.models.entities.Assessment;
import com.example.schooltracker.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NewAssessmentVM extends ViewModel {
    private Assessment a = null;
    private static final String TAG = "NewAssessmentVM";

    public MutableLiveData<String> title = new MutableLiveData<>();
    public MutableLiveData<String> startDate = new MutableLiveData<>();
    public MutableLiveData<String> endDate= new MutableLiveData<>();
    public MutableLiveData<Boolean> performanceAssessment= new MutableLiveData<>();
    public MutableLiveData<Boolean> objectiveAssessment= new MutableLiveData<>();
    public MutableLiveData<Boolean> alertSlider= new MutableLiveData<>(false);

    public Assessment.TYPE typeOfAssessment;

    public boolean isValid(){
        if(startDate.getValue() == null || startDate.getValue().isEmpty() ||
                endDate.getValue() == null || endDate.getValue().isEmpty() ||
                    performanceAssessment.getValue() == null || objectiveAssessment.getValue() == null || title.getValue() == null || title.getValue().isEmpty()) {
            return false;
        }
        return true;
    }

    public Assessment getAssessment(){
        if(performanceAssessment.getValue()){
            typeOfAssessment = Assessment.TYPE.PERFORMANCE;
        }else {
            typeOfAssessment = Assessment.TYPE.OBJECTIVE;
        }
        if (this.a == null){
            this.a = new Assessment(title.getValue(), Utils.getDateString(startDate.getValue()), Utils.getDateString(endDate.getValue()), typeOfAssessment, alertSlider.getValue());
            //if null create new
        }else{
            this.a.setTitle(title.getValue());
            this.a.setStart(Utils.getDateString(startDate.getValue()));
            this.a.setEnd(Utils.getDateString(endDate.getValue()));
            this.a.setType(typeOfAssessment);
            this.a.setAlert(alertSlider.getValue());
        }
        return this.a;
    }

    public void setPerformanceAssessment(boolean active){
        if(active == true){
            this.performanceAssessment.setValue(active);
            this.objectiveAssessment.setValue(!active);
        }else if(active == false){
            this.performanceAssessment.setValue(active);;
            this.objectiveAssessment.setValue(!active);
        }
    }

    public void setAssessment(Assessment assessment){
        this.a = assessment;
        this.title.setValue(a.getTitle());
        this.startDate.setValue(Utils.getStringFromDate(a.getStart()));
        this.endDate.setValue(Utils.getStringFromDate(a.getEnd()));
        switch (this.a.getType()){
            case PERFORMANCE:
                this.performanceAssessment.setValue(true);
                this.objectiveAssessment.setValue(false);
                break;
            case OBJECTIVE:
                this.objectiveAssessment.setValue(true);
                this.performanceAssessment.setValue(false);
                break;
        }
        this.alertSlider.setValue(this.a.isAlert());
    }

    public void setStartDate(Calendar cal) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/YYYY", Locale.US);
        this.startDate.setValue(sdf.format(cal.getTime()));
    }

    public void setEndDate(Calendar cal) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/YYYY", Locale.US);
        this.endDate.setValue(sdf.format(cal.getTime()));
    }
}
