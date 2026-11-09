package com.example.smartstudentattendancetracker.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartstudentattendancetracker.R;
import com.example.smartstudentattendancetracker.database.SQLiteHelper;
import com.example.smartstudentattendancetracker.session.SessionManager;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AdminDashboardActivity extends AppCompatActivity {

    private TextView welcomeAdmin, textCurrentDate;
    private TextView textTotalCount, textPresentCount, textAbsentCount, textLeaveCount;
    private TextView adminOverallPercentText;
    private CircularProgressIndicator adminOverallProgress;
    private View actionStudents, actionMark, actionReports, actionNotice, actionTimetable, actionAnalytics, actionHistory, actionMore, adminProfileCard;
    private MaterialButton btnLogout;
    private BarChart barChart;
    private SessionManager session;
    private SQLiteHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        session = new SessionManager(this);
        database = new SQLiteHelper(this);
        initViews();
        setupClickListeners();
        setupChart();
    }

    private void initViews() {
        welcomeAdmin = findViewById(R.id.welcomeAdmin);
        textCurrentDate = findViewById(R.id.textCurrentDate);
        
        textTotalCount = findViewById(R.id.textTotalStudentsCount);
        textPresentCount = findViewById(R.id.textPresentToday);
        textAbsentCount = findViewById(R.id.textAbsentToday);
        textLeaveCount = findViewById(R.id.textLeaveToday);

        adminOverallProgress = findViewById(R.id.adminOverallProgress);
        adminOverallPercentText = findViewById(R.id.adminOverallPercentText);
        
        actionStudents = findViewById(R.id.cardManageStudents);
        actionMark = findViewById(R.id.cardMarkAttendance);
        actionReports = findViewById(R.id.cardAdminReports);
        actionNotice = findViewById(R.id.cardPostNotice);
        actionTimetable = findViewById(R.id.cardManageTimetable);
        actionAnalytics = findViewById(R.id.cardAdminAnalytics);
        actionHistory = findViewById(R.id.cardAdminAttendanceHistory);
        actionMore = findViewById(R.id.cardAdminMore);
        adminProfileCard = findViewById(R.id.adminProfileCard);
        
        btnLogout = findViewById(R.id.cardAdminLogout);
        barChart = findViewById(R.id.barChartConducted);

        // Setup individual action labels and icons
        setupAction(actionStudents, "Students", android.R.drawable.ic_menu_myplaces);
        setupAction(actionMark, "Attendance", R.drawable.ic_attendance);
        setupAction(actionReports, "Reports", R.drawable.ic_analytics);
        setupAction(actionNotice, "Notice", R.drawable.ic_notice);
        setupAction(actionTimetable, "Timetable", R.drawable.ic_timetable);
        setupAction(actionAnalytics, "Analytics", R.drawable.ic_analytics);
        setupAction(actionHistory, "History", R.drawable.ic_attendance);
        setupAction(actionMore, "More", android.R.drawable.ic_menu_more);
    }

    private void setupAction(View view, String text, int iconRes) {
        if (view == null) return;
        TextView tv = view.findViewById(R.id.actionText);
        ImageView iv = view.findViewById(R.id.actionIcon);
        if (tv != null) tv.setText(text);
        if (iv != null) iv.setImageResource(iconRes);
    }

    private void setupClickListeners() {
        if (actionStudents != null) actionStudents.setOnClickListener(v -> startActivity(new Intent(this, StudentManagementActivity.class)));
        if (actionMark != null) actionMark.setOnClickListener(v -> startActivity(new Intent(this, AdminAttendanceActivity.class)));
        if (actionReports != null) actionReports.setOnClickListener(v -> startActivity(new Intent(this, ReportActivity.class)));
        if (actionNotice != null) actionNotice.setOnClickListener(v -> startActivity(new Intent(this, NoticeActivity.class)));
        if (actionTimetable != null) actionTimetable.setOnClickListener(v -> startActivity(new Intent(this, TimetableActivity.class)));
        if (actionAnalytics != null) actionAnalytics.setOnClickListener(v -> startActivity(new Intent(this, AnalyticsActivity.class)));
        if (actionHistory != null) actionHistory.setOnClickListener(v -> startActivity(new Intent(this, AttendanceHistoryActivity.class)));
        if (actionMore != null || adminProfileCard != null) {
            View.OnClickListener profileClick = v -> {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("fragment", "profile");
                startActivity(intent);
            };
            if (actionMore != null) actionMore.setOnClickListener(profileClick);
            if (adminProfileCard != null) adminProfileCard.setOnClickListener(profileClick);
        }

        if (btnLogout != null) btnLogout.setOnClickListener(v -> {
            session.logout();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void setupChart() {
        if (barChart == null) return;
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1, 40f));
        entries.add(new BarEntry(2, 60f));
        entries.add(new BarEntry(3, 45f));
        entries.add(new BarEntry(4, 70f));
        entries.add(new BarEntry(5, 55f));

        BarDataSet set = new BarDataSet(entries, "");
        set.setColor(android.graphics.Color.WHITE);
        set.setDrawValues(false);

        BarData data = new BarData(set);
        data.setBarWidth(0.5f);
        barChart.setData(data);
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.getXAxis().setEnabled(false);
        barChart.getAxisLeft().setTextColor(android.graphics.Color.WHITE);
        barChart.getAxisRight().setEnabled(false);
        barChart.invalidate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
        loadProfileImage();
    }

    private void loadProfileImage() {
        String email = session.getUserEmail();
        Cursor c = null;
        try {
            c = database.getProfile(email);
            if (c != null && c.moveToFirst()) {
                String path = c.getString(10);
                if (path != null && !path.isEmpty()) {
                    java.io.File file = new java.io.File(path);
                    if (file.exists()) {
                        ImageView iv = findViewById(R.id.adminProfileImage);
                        if (iv != null) iv.setImageURI(android.net.Uri.fromFile(file));
                    }
                }
            }
        } finally {
            if (c != null) c.close();
        }
    }

    private void updateUI() {
        String email = session.getUserEmail();
        String name = database.getUserName(email);
        
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        String greeting;
        if (timeOfDay >= 5 && timeOfDay < 12) greeting = "Good Morning";
        else if (timeOfDay >= 12 && timeOfDay < 17) greeting = "Good Afternoon";
        else if (timeOfDay >= 17 && timeOfDay < 21) greeting = "Good Evening";
        else greeting = "Good Night";
        
        welcomeAdmin.setText(greeting + ", " + (name.isEmpty() ? "Admin" : name) + " 👋");
        
        String formattedDate = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date());
        textCurrentDate.setText(formattedDate);

        int total = database.getRegisteredStudentsCount();
        textTotalCount.setText(String.valueOf(total));
        
        int[] todayStats = database.getInstitutionalStatsToday();
        textPresentCount.setText(String.valueOf(todayStats[0]));
        textAbsentCount.setText(String.valueOf(todayStats[1]));
        textLeaveCount.setText(String.valueOf(todayStats[2]));

        // Institutional Average
        int avg = 0;
        Cursor cAll = null;
        try {
            cAll = database.getReadableDatabase().rawQuery("SELECT email FROM users WHERE role='STUDENT'", null);
            if (cAll != null && cAll.moveToFirst()) {
                int sum = 0, count = 0;
                do {
                    sum += database.getOverallPercentage(cAll.getString(0));
                    count++;
                } while (cAll.moveToNext());
                if (count > 0) avg = sum / count;
            }
        } finally {
            if (cAll != null) cAll.close();
        }
        
        adminOverallPercentText.setText(avg + "%");
        adminOverallProgress.setProgress(avg, true);
    }
}
