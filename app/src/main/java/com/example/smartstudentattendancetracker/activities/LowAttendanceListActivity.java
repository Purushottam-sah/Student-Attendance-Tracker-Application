package com.example.smartstudentattendancetracker.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstudentattendancetracker.R;
import com.example.smartstudentattendancetracker.adapters.StudentAdapter;
import com.example.smartstudentattendancetracker.database.SQLiteHelper;
import com.example.smartstudentattendancetracker.models.Student;

import java.util.ArrayList;

public class LowAttendanceListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SQLiteHelper database;
    private ArrayList<Student> studentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_low_attendance_list);

        Toolbar toolbar = findViewById(R.id.toolbarLowAttendance);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        database = new SQLiteHelper(this);
        recyclerView = findViewById(R.id.recyclerLowAttendance);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.btnNotifyAll).setOnClickListener(v -> 
            Toast.makeText(this, "Notifications sent to all low attendance students", Toast.LENGTH_LONG).show());

        loadLowAttendanceStudents();
    }

    private void loadLowAttendanceStudents() {
        studentList = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = database.getReadableDatabase().rawQuery(
                "SELECT DISTINCT students.id, students.name, students.email, students.rollNumber, students.className " +
                "FROM students INNER JOIN subjects ON lower(students.email) = lower(subjects.userEmail) " +
                "WHERE (subjects.presentClass * 100 / CASE WHEN subjects.totalClass = 0 THEN 1 ELSE subjects.totalClass END) < 75 " +
                "AND subjects.totalClass > 0", null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    String name = cursor.getString(1);
                    String email = cursor.getString(2);
                    String roll = cursor.getString(3);
                    String className = cursor.getString(4);
                    studentList.add(new Student(
                        id,
                        name != null ? name : "",
                        email != null ? email : "",
                        roll != null ? roll : "",
                        className != null ? className : ""
                    ));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) cursor.close();
        }

        StudentAdapter adapter = new StudentAdapter(studentList, null); 
        recyclerView.setAdapter(adapter);
    }
}