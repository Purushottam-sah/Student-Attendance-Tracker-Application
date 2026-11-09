package com.example.smartstudentattendancetracker.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartstudentattendancetracker.R;
import com.example.smartstudentattendancetracker.database.SQLiteHelper;
import com.example.smartstudentattendancetracker.notifications.NotificationHelper;
import com.example.smartstudentattendancetracker.session.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AttendanceActivity extends AppCompatActivity {

    private Button present, absent;
    private TextView subjectTitle, textRestriction;
    private SQLiteHelper database;
    private SessionManager session;
    private int subjectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        subjectId = getIntent().getIntExtra("subjectId", -1);
        String subjectName = getIntent().getStringExtra("subjectName");

        subjectTitle = findViewById(R.id.subjectTitle);
        present = findViewById(R.id.presentBtn);
        absent = findViewById(R.id.absentBtn);
        textRestriction = findViewById(R.id.textRestriction);

        if (subjectName != null) {
            subjectTitle.setText(subjectName);
        }

        database = new SQLiteHelper(this);
        session = new SessionManager(this);

        setupRoleBasedUI();

        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        present.setOnClickListener(v -> mark("Present", subjectName, date));
        absent.setOnClickListener(v -> mark("Absent", subjectName, date));
    }

    private void setupRoleBasedUI() {
        String role = database.getUserRole(session.getUserEmail());
        if (role.equals("STUDENT")) {
            present.setVisibility(View.GONE);
            absent.setVisibility(View.GONE);
            if (textRestriction != null) {
                textRestriction.setVisibility(View.VISIBLE);
                textRestriction.setText("Attendance is managed by Administrator");
            }
        }
    }

    private void mark(String status, String subjectName, String date) {
        if (subjectId != -1) {
            String email = session.getUserEmail();
            if (database.addAttendance(subjectId, date, status, email)) {
                NotificationHelper.showAttendanceMarkedNotification(this, subjectName, status);
                
                int presentCount = database.getPresentCount(subjectId);
                int totalCount = database.getTotalAttendance(subjectId);
                int percent = (totalCount == 0) ? 0 : (presentCount * 100) / totalCount;
                
                if (percent < 75 && totalCount >= 5) {
                    NotificationHelper.showLowAttendanceWarning(this, subjectName, percent);
                }

                Toast.makeText(this, status + " Marked", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}