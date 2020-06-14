package com.example.schooltracker.utils.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.schooltracker.R;

public class NotificationHelper extends ContextWrapper {
    private static final String TAG = "NotificationHelper";
    private static final String channelID = "schooltracker.notification";
    private static final String channelName = "Notifications";

    private NotificationManager manager;

    public NotificationHelper(Context base) {
        super(base);
        createChannel();
    }

    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_DEFAULT);
        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (this.manager == null) {
            Log.d(TAG, "getManager: creating new manager");
            this.manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return this.manager;
    }

    public NotificationCompat.Builder getChannelNotification(String title, String content) {
        Log.d(TAG, "getChannelNotification: creating notification");
        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.drawable.ic_add);
    }
}
