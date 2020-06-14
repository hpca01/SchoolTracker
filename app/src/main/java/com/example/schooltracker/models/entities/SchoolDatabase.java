package com.example.schooltracker.models.entities;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.schooltracker.models.entities.DAO.SchoolDao;
import com.example.schooltracker.models.entities.convertor.AssessmentTypeConvertor;
import com.example.schooltracker.models.entities.convertor.CourseStatusConvertor;
import com.example.schooltracker.models.entities.convertor.DateConvertor;

@Database(entities = {Term.class, Course.class, Note.class, Assessment.class, Mentor.class, CourseWithMentor.class}, version = 7, exportSchema = false)
@TypeConverters({AssessmentTypeConvertor.class, CourseStatusConvertor.class, DateConvertor.class})
public abstract class SchoolDatabase extends RoomDatabase {

    public abstract SchoolDao schoolDao();

    private static SchoolDatabase INSTANCE = null;

    public static SchoolDatabase getDb(final Context context){
        if (INSTANCE == null){
            synchronized (SchoolDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), SchoolDatabase.class, "school_database").fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }



}
