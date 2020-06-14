package com.example.schooltracker;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.schooltracker.utils.notification.NotificationHelper;

public class GoalAlertReceiver extends BroadcastReceiver {
    private static final String TAG = "GoalAlertReceiver";
    private static int notificationID;

    public final static String TERM = "TERM";
    public final static String COURSE = "COURSE";
    public final static String ASSESSMENT = "ASSESSMENT";

    @Override
    public void onReceive(Context context, Intent intent) {
        String type = intent.getStringExtra("ALERT_TYPE");
        NotificationHelper helper = new NotificationHelper(context);
        if (type != null && !type.isEmpty()) {
            switch (type) {
                case TERM:
                    String termName = intent.getStringExtra("term_name");
                    String termStart = intent.getStringExtra("term_start");
                    NotificationCompat.Builder nb = helper.getChannelNotification("Term start alert", termName + " " +termStart);
                    helper.getManager().notify(notificationID++, nb.build());
                    break;
                case COURSE:
                    String courseName = intent.getStringExtra("course_name");
                    boolean courseStart = intent.getBooleanExtra("course_start", true);
                    StringBuilder builder = new StringBuilder();
                    builder.append("Alert for ");
                    builder.append(courseName);
                    builder.append(" course. ");
                    builder.append(courseStart? "Course Start. ":"Course End. ");
                    builder.append("Please navigate to the schooltracker app");
                    NotificationCompat.Builder courseNotification = helper.getChannelNotification("Course alert", builder.toString());
                    helper.getManager().notify(notificationID++, courseNotification.build());
                    break;
                case ASSESSMENT:
                    String assessmentName = intent.getStringExtra("assessment_name");
                    String assessmentType = intent.getStringExtra("assessment_type");
                    boolean start = intent.getBooleanExtra("assessment_start", true);
                    builder = new StringBuilder();
                    builder.append(assessmentName+" ");
                    builder.append(assessmentType);
                    builder.append(start? " Assessment Start. ":" Assessment End. ");
                    builder.append("Please navigate to the schooltracker app");
                    NotificationCompat.Builder assessmentNotification = helper.getChannelNotification("Assessment alert", builder.toString());
                    helper.getManager().notify(notificationID++, assessmentNotification.build());
                    break;
            }
        }

    }
}
