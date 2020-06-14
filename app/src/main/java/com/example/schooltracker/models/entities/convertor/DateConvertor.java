package com.example.schooltracker.models.entities.convertor;

import androidx.room.TypeConverter;

import java.util.Date;

public class DateConvertor {
    @TypeConverter
    public static Date fromTimestamp(Long value){
        if (value == null){
            return null;
        }else{
            return new Date(value);
        }
    }
    @TypeConverter
    public static Long dateToTimeStamp(Date date){
        if (date == null){
            return null;
        }else{
            return date.toInstant().toEpochMilli();
        }
    }
}
