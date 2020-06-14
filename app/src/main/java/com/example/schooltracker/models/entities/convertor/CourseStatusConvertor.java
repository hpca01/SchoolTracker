package com.example.schooltracker.models.entities.convertor;

import androidx.room.TypeConverter;

import com.example.schooltracker.models.entities.Course;

public class CourseStatusConvertor {

    @TypeConverter
    public static Course.STATUS fromInt(Integer value){
        if (value==null) return null;
        else{
            if(value == Course.STATUS.IN_PROGRESS.getValue()){
                return Course.STATUS.IN_PROGRESS;
            }else if(value == Course.STATUS.COMPLETED.getValue()){
                return Course.STATUS.COMPLETED;
            }else if(value == Course.STATUS.PLANNED.getValue()){
                return Course.STATUS.PLANNED;
            }else{
                return Course.STATUS.DROPPED;
            }
        }
    }

    @TypeConverter
    public static int fromStatus(Course.STATUS stat){
        return stat.getValue();
    }
}
