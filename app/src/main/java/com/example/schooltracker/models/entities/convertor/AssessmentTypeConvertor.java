package com.example.schooltracker.models.entities.convertor;

import androidx.room.TypeConverter;

import com.example.schooltracker.models.entities.Assessment;

public class AssessmentTypeConvertor {
    @TypeConverter
    public static Assessment.TYPE fromValue(Integer value){
        if (value == null){
            return null;
        }else{
            if(value == Assessment.TYPE.PERFORMANCE.getValue()){
                return Assessment.TYPE.PERFORMANCE;
            }else{
                return Assessment.TYPE.OBJECTIVE;
            }
        }
    }

    @TypeConverter
    public static int toInt(Assessment.TYPE type){
        return type.getValue();
    }
}
