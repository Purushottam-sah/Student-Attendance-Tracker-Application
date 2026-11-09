package com.example.smartstudentattendancetracker.activities;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstudentattendancetracker.R;
import com.example.smartstudentattendancetracker.adapters.BatchAttendanceAdapter;
import com.example.smartstudentattendancetracker.database.SQLiteHelper;
import com.example.smartstudentattendancetracker.models.Student;
import com.example.smartstudentattendancetracker.notifications.NotificationHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdminAttendanceActivity extends AppCompatActivity {

    private AutoCompleteTextView spinnerClass, spinnerSubject;
    private EditText editDate;
    private Button btnLoad, btnSubmit;
    private ImageButton btnBack;
    private RecyclerView recyclerView;
    private SQLiteHelper database;
    private ArrayList<Student> studentList;
    private BatchAttendanceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_attendance);

        database = new SQLiteHelper(this);
        initViews();
        setupAdapters();

        // Default current date
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        editDate.setText(currentDate);

        editDate.setOnClickListener(v -> showDatePicker());
        btnLoad.setOnClickListener(v -> loadBatch());
        btnSubmit.setOnClickListener(v -> submitAttendance());
        btnBack.setOnClickListener(v -> finish());
    }

    private void initViews() {
        spinnerClass = findViewById(R.id.spinnerClass);
        spinnerSubject = findViewById(R.id.spinnerSubject);
        editDate = findViewById(R.id.editAttendanceDate);
        btnLoad = findViewById(R.id.btnLoadStudents);
        btnSubmit = findViewById(R.id.btnSubmitAttendance);
        btnBack = findViewById(R.id.btnBack);
        recyclerView = findViewById(R.id.recyclerBatchAttendance);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupAdapters() {
        List<String> classes = database.getAllClasses();
        // Fallback removed, list should be dynamic
        
        ArrayAdapter<String> classAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, classes);
        spinnerClass.setAdapter(classAdapter);

        List<String> subjects = database.getAllSubjectNames();
        // Fallback for subjects
        if (subjects.isEmpty()) {
            subjects.add("All Subjects");
        }
        
        ArrayAdapter<String> subAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, subjects);
        spinnerSubject.setAdapter(subAdapter);
    }

    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            Calendar selected = Calendar.getInstance();
            selected.set(year, month, dayOfMonth);
            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selected.getTime());
            editDate.setText(date);
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void loadBatch() {
        String className = spinnerClass.getText().toString();
        if (className.isEmpty()) {
            Toast.makeText(this, "Select a class first", Toast.LENGTH_SHORT).show();
            return;
        }

        studentList = new ArrayList<>();
        Cursor cursor = database.getStudentsByClass(className);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                studentList.add(new Student(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4)
                ));
            } while (cursor.moveToNext());
            cursor.close();
        }

        if (studentList.isEmpty()) {
            Toast.makeText(this, "No students found in this class", Toast.LENGTH_SHORT).show();
            btnSubmit.setVisibility(View.GONE);
        } else {
            adapter = new BatchAttendanceAdapter(studentList);
            recyclerView.setAdapter(adapter);
            btnSubmit.setVisibility(View.VISIBLE);
        }
    }

    private void submitAttendance() {
        if (studentList == null || studentList.isEmpty()) return;

        String className = spinnerClass.getText().toString();
        String subjectName = spinnerSubject.getText().toString();
        String date = editDate.getText().toString();

        if (subjectName.isEmpty()) {
            Toast.makeText(this, "Select a subject", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success = true;
        for (int i = 0; i < studentList.size(); i++) {
            String status = adapter.getAttendanceStatus(i);
            String studentEmail = studentList.get(i).getEmail();
            String studentName = studentList.get(i).getName();
            
            if (database.markAttendance(studentEmail, className, subjectName, date, status)) {
                // Trigger a system notification (on this device)
                NotificationHelper.showAttendanceMarkedNotification(this, subjectName + " (" + studentName + ")", status);
                
                // Store in notifications table for student to see on their device
                database.addNotification("Attendance Update", "You were marked " + status + " for " + subjectName, "Attendance", studentEmail);
                
                // Also store as internal institutional notice
                database.addNotice("Attendance Update", studentName + ": Marked as " + status + " for " + subjectName, date, "Admin", "Attendance");
            } else {
                success = false;
            }
        }

        if (success) {
            NotificationHelper.showAttendanceMarkedNotification(this, subjectName, "Batch attendance saved");
            Toast.makeText(this, "Attendance marked for all students", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Some entries failed", Toast.LENGTH_SHORT).show();
        }
    }
}