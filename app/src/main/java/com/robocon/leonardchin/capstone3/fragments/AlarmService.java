package com.robocon.leonardchin.capstone3.fragments;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.robocon.leonardchin.capstone3.MainActivity;
import com.robocon.leonardchin.capstone3.R;

public class AlarmService extends IntentService {
    private NotificationManager alarmNotificationManager;

    public AlarmService() {
        super("AlarmService");
    }

    @Override
    public void onHandleIntent(Intent intent) {
        sendNotification("Your parking period is expired...");
    }

    private void sendNotification(String msg) {
        Log.d("AlarmService", "Preparing to send notification...: " + msg);
        alarmNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);
        long[] pattern = {500,500,500,500,500,500,500,500,500};
        NotificationCompat.Builder alamNotificationBuilder = new NotificationCompat.Builder(
                this).setContentTitle("Notification")
                .setSmallIcon(R.mipmap.ic_launcher1_foreground)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notificationsound))
                .setContentText(msg)
                .setVibrate(pattern)
                .setAutoCancel(true);


        alamNotificationBuilder.setContentIntent(contentIntent);
        alarmNotificationManager.notify(1, alamNotificationBuilder.build());
        Log.d("AlarmService", "Notification sent.");
    }
}