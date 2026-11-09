package com.example.smartstudentattendancetracker.activities;

import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smartstudentattendancetracker.R;
import com.example.smartstudentattendancetracker.adapters.NotificationAdapter;
import com.example.smartstudentattendancetracker.database.SQLiteHelper;
import com.example.smartstudentattendancetracker.models.Notification;
import com.example.smartstudentattendancetracker.session.SessionManager;
import java.util.ArrayList;
import java.util.List;

public class NotificationListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<Notification> notificationList;
    private SQLiteHelper database;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);

        database = new SQLiteHelper(this);
        session = new SessionManager(this);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnMarkAllRead).setOnClickListener(v -> markAllRead());

        recyclerView = findViewById(R.id.recyclerNotifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadNotifications();
    }

    private void loadNotifications() {
        notificationList = new ArrayList<>();
        String email = session.getUserEmail();
        Cursor cursor = null;
        try {
            cursor = database.getNotifications(email);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    String title = cursor.getString(1);
                    String message = cursor.getString(2);
                    String date = cursor.getString(3);
                    String typeStr = cursor.getString(4);
                    boolean isRead = cursor.getInt(6) == 1;

                    int type = 0;
                    if ("Attendance".equalsIgnoreCase(typeStr)) type = 0;
                    else if ("Warning".equalsIgnoreCase(typeStr)) type = 1;
                    else if ("Notice".equalsIgnoreCase(typeStr)) type = 2;
                    else if ("Timetable".equalsIgnoreCase(typeStr)) type = 3;

                    notificationList.add(new Notification(title != null ? title : "Notification",
                            message != null ? message : "",
                            date != null ? date : "Just now",
                            isRead, type));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) cursor.close();
        }

        if (notificationList.isEmpty()) {
            notificationList.add(new Notification("Attendance Marked", "Your attendance for Mathematics has been marked as Present.", "10 mins ago", false, 0));
            notificationList.add(new Notification("Low Attendance Warning", "Your attendance in Physics is below 75%. Please attend more classes.", "2 hours ago", false, 1));
            notificationList.add(new Notification("New Notice", "The college will remain closed tomorrow due to public holiday.", "5 hours ago", true, 2));
            notificationList.add(new Notification("Timetable Updated", "The timetable for Semester 6 has been updated. Check now.", "1 day ago", true, 3));
        }

        adapter = new NotificationAdapter(notificationList);
        recyclerView.setAdapter(adapter);
    }

    private void markAllRead() {
        for (Notification n : notificationList) {
            n.setRead(true);
        }
        if (adapter != null) adapter.notifyDataSetChanged();
    }
}