package com.example.smartstudentattendancetracker.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartstudentattendancetracker.R;
import com.example.smartstudentattendancetracker.database.SQLiteHelper;
import com.example.smartstudentattendancetracker.session.SessionManager;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.ArrayList;
import java.util.Locale;

public class ReportActivity extends AppCompatActivity {

    private PieChart pieChart;
    private LinearLayout subjectListContainer;
    private TextView txtTotal, txtConducted, txtAvg;
    private SQLiteHelper database;
    private SessionManager session;
    private String studentEmail;
    private boolean isAdminView = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        database = new SQLiteHelper(this);
        session = new SessionManager(this);
        
        String role = database.getUserRole(session.getUserEmail());
        isAdminView = "ADMIN".equals(role);

        studentEmail = getIntent().getStringExtra("userEmail");
        if (studentEmail == null) {
            studentEmail = session.getUserEmail();
        }

        initViews();
        setupToggleListener();
        
        updateReport();
    }

    private void initViews() {
        pieChart = findViewById(R.id.pieChartOverall);
        subjectListContainer = findViewById(R.id.subjectListContainer);
        
        txtTotal = findViewById(R.id.txtTotalClasses);
        txtConducted = findViewById(R.id.txtConductedClasses);
        txtAvg = findViewById(R.id.txtAvgAttendance);

        View toolbar = findViewById(R.id.toolbarReport);
        if (toolbar != null) {
            toolbar.setOnClickListener(v -> finish());
        }
    }

    private void setupToggleListener() {
        MaterialButtonToggleGroup toggle = findViewById(R.id.toggleReportPeriod);
        if (toggle != null) {
            toggle.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
                if (isChecked) updateReport();
            });
        }
    }

    private void updateReport() {
        int present, totalRecords, leave, conducted, avg;

        if (isAdminView && studentEmail.equals(session.getUserEmail())) {
            // Institutional wide summary
            totalRecords = 0; present = 0; leave = 0;
            Cursor c = database.getReadableDatabase().rawQuery("SELECT email FROM users WHERE role='STUDENT'", null);
            if (c.moveToFirst()) {
                do {
                    String email = c.getString(0);
                    totalRecords += database.getAllTotalAttendanceCount(email);
                    present += database.getAllPresentCount(email);
                    leave += database.getAllLeaveCount(email);
                } while (c.moveToNext());
            }
            c.close();
            conducted = totalRecords - leave;
            avg = conducted == 0 ? 0 : (present * 100) / conducted;
        } else {
            // Specific student summary
            present = database.getAllPresentCount(studentEmail);
            leave = database.getAllLeaveCount(studentEmail);
            totalRecords = database.getAllTotalAttendanceCount(studentEmail);
            conducted = totalRecords - leave;
            avg = database.getOverallPercentage(studentEmail);
        }

        txtTotal.setText(String.valueOf(totalRecords));
        txtConducted.setText(String.valueOf(conducted));
        txtAvg.setText(avg + "%");

        setupPieChart(present, totalRecords - present - leave, leave);
        setupSubjectProgressList();
    }

    private void setupPieChart(int present, int absent, int leave) {
        int total = present + absent + leave;
        if (total == 0) {
            pieChart.setNoDataText("No records recorded yet.");
            pieChart.invalidate();
            return;
        }

        ArrayList<PieEntry> entries = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        
        if (present > 0) {
            entries.add(new PieEntry(present, ""));
            colors.add(getResources().getColor(R.color.present_green, getTheme()));
        }
        if (absent > 0) {
            entries.add(new PieEntry(absent, ""));
            colors.add(getResources().getColor(R.color.absent_red, getTheme()));
        }
        if (leave > 0) {
            entries.add(new PieEntry(leave, ""));
            colors.add(getResources().getColor(R.color.leave_orange, getTheme()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colors);
        dataSet.setDrawValues(false);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.setHoleRadius(75f);
        pieChart.setCenterText("Attendance");
        pieChart.animateY(1000);
        pieChart.invalidate();
    }

    private void setupSubjectProgressList() {
        if (subjectListContainer == null) return;
        subjectListContainer.removeAllViews();
        
        Cursor cursor;
        if (isAdminView && studentEmail.equals(session.getUserEmail())) {
            // Show top overall subjects for Admin
            cursor = database.getReadableDatabase().rawQuery("SELECT DISTINCT name, SUM(totalClass), SUM(presentClass) FROM subjects GROUP BY name ORDER BY SUM(presentClass) DESC LIMIT 5", null);
        } else {
            // Show subjects for current targeted student
            cursor = database.getSubjects(studentEmail);
        }
        
        LayoutInflater inflater = LayoutInflater.from(this);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String name;
                int total, present;

                if (isAdminView && studentEmail.equals(session.getUserEmail())) {
                    // Results from: SELECT name, SUM(total), SUM(present)
                    name = cursor.getString(0);
                    total = cursor.getInt(1);
                    present = cursor.getInt(2);
                } else {
                    // Results from: SELECT * FROM subjects (id, name, total, present...)
                    name = cursor.getString(1);
                    total = cursor.getInt(2);
                    present = cursor.getInt(3);
                }
                
                int percent = (total == 0) ? 0 : (present * 100) / total;

                View itemView = inflater.inflate(R.layout.item_subject_progress, subjectListContainer, false);
                ((TextView)itemView.findViewById(R.id.subjectName)).setText(name);
                ((TextView)itemView.findViewById(R.id.subjectPercent)).setText(percent + "%");
                ((ProgressBar)itemView.findViewById(R.id.subjectProgress)).setProgress(percent);

                subjectListContainer.addView(itemView);
            } while (cursor.moveToNext());
            cursor.close();
        }
    }
}