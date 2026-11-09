package com.example.smartstudentattendancetracker.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartstudentattendancetracker.R;
import com.example.smartstudentattendancetracker.database.SQLiteHelper;
import com.example.smartstudentattendancetracker.session.SessionManager;
import com.google.android.material.progressindicator.CircularProgressIndicator;

public class LowAttendanceAlertActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_low_attendance_alert);

        SQLiteHelper database = new SQLiteHelper(this);
        SessionManager session = new SessionManager(this);
        String email = session.getUserEmail();

        int present = database.getAllPresentCount(email);
        int total = database.getAllTotalAttendanceCount(email);
        int percent = database.getOverallPercentage(email);

        CircularProgressIndicator progress = findViewById(R.id.alertProgress);
        TextView txtPercent = findViewById(R.id.alertPercentText);
        TextView txtNeeded = findViewById(R.id.txtClassesNeeded);

        progress.setProgress(percent);
        txtPercent.setText(percent + "%");

        // Calculate classes needed to reach 75%
        // (present + x) / (total + x) = 0.75  =>  x = 3*total - 4*present
        int needed = (3 * total) - (4 * present);
        if (needed < 0) needed = 0;
        
        txtNeeded.setText(String.valueOf(needed));

        findViewById(R.id.btnContactAdvisor).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:advisor@college.edu"));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Attendance Concern - " + email);
            startActivity(intent);
        });

        findViewById(R.id.btnCloseAlert).setOnClickListener(v -> finish());
    }
}