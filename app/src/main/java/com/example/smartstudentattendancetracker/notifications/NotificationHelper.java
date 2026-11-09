package com.example.smartstudentattendancetracker.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.smartstudentattendancetracker.R;

public class NotificationHelper {

    private static final String REMINDER_CHANNEL_ID = "attendance_reminder";
    private static final String STATUS_CHANNEL_ID = "attendance_status";

    public static void showAttendanceReminder(Context context) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    REMINDER_CHANNEL_ID,
                    "Daily Reminders",
                    NotificationManager.IMPORTANCE_HIGH
            );
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, REMINDER_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Mark Attendance")
                .setContentText("Don't forget to mark today's attendance")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        manager.notify(101, builder.build());
    }

    public static void showAttendanceMarkedNotification(Context context, String subjectName, String status) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    STATUS_CHANNEL_ID,
                    "Attendance Status",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, STATUS_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Attendance Marked")
                .setContentText(subjectName + ": " + status)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        manager.notify(102, builder.build());
    }

    public static void showLowAttendanceWarning(Context context, String subjectName, int percentage) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    STATUS_CHANNEL_ID,
                    "Attendance Warnings",
                    NotificationManager.IMPORTANCE_HIGH
            );
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, STATUS_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Low Attendance Alert")
                .setContentText("Your attendance in " + subjectName + " is " + percentage + "%. Time to attend more classes!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        manager.notify(103, builder.build());
    }
}