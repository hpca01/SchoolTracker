package com.example.schooltracker.utils;

import android.app.AlertDialog;
import android.app.Application;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Entity;

import com.example.schooltracker.NewTerm;
import com.example.schooltracker.R;
import com.example.schooltracker.models.entities.Course;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Utils {
    public static String getString(EditText text){
        return text.getText().toString();
    }

    public static Date getDateString(EditText text){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        try {
            Date parsedDate = sdf.parse(text.getText().toString());
            return parsedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date getDateString(String text){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        try {
            Date parsedDate = sdf.parse(text);
            return parsedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getStringFromDate(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        return sdf.format(date);
    }

    public static String getStatusString(Course.STATUS status){
        switch(status){
            case COMPLETED:
                return "Completed";
            case IN_PROGRESS:
                return "In Progress";
            case PLANNED:
                return "Planned";
            case DROPPED:
                return "Dropped";
        }
        return null;
    }

    public static Integer getStatusColor(Resources resources, Course.STATUS status){
        switch(status){
            case IN_PROGRESS:
                return ResourcesCompat.getColor(resources, R.color.courseInProgress, null);
            case COMPLETED:
                return ResourcesCompat.getColor(resources, R.color.courseCompleted, null);
            case PLANNED:
                return ResourcesCompat.getColor(resources, R.color.coursePlanned, null);
            case DROPPED:
                return ResourcesCompat.getColor(resources, R.color.courseDropped, null);
            default:
                return ResourcesCompat.getColor(resources, R.color.courseInProgress, null);
        }
    }

    public static <T extends LinearLayoutManager> void addDividers(RecyclerView recycler, T layoutManager){
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recycler.getContext(), layoutManager.getOrientation());
        recycler.addItemDecoration(dividerItemDecoration);
    }

    public static <T extends GridLayoutManager> void addDividers(RecyclerView recycler, T layoutManager){
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recycler.getContext(), layoutManager.getOrientation());
        recycler.addItemDecoration(dividerItemDecoration);
    }
    public static <T , E extends AndroidViewModel & CRUD, C extends RecyclerView> ItemTouchHelper makeSwipeHelper(List<T> objects, E viewModel, C recycler){
        ItemTouchHelper.SimpleCallback itemTouchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();
                T object = objects.get(pos);
                objects.remove(object);
                recycler.getAdapter().notifyItemRemoved(pos);
                viewModel.delete(object);
            }
        };
        return new ItemTouchHelper(itemTouchHelper);
    }
}
