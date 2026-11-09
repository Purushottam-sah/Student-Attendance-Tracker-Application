package com.example.smartstudentattendancetracker.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartstudentattendancetracker.R;
import com.example.smartstudentattendancetracker.database.SQLiteHelper;

public class AdminStudentSubjectsActivity extends AppCompatActivity {

    private TextView textName, textClass, textEmail, textPhone, textCourse, textSem;
    private SQLiteHelper database;
    private String studentEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_student_subjects);

        database = new SQLiteHelper(this);
        initViews();

        String name = getIntent().getStringExtra("name");
        studentEmail = getIntent().getStringExtra("email");

        loadStudentData(name, studentEmail);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        
        findViewById(R.id.btnAdminAttendance).setOnClickListener(v -> {
            Intent intent = new Intent(this, AttendanceActivity.class);
            intent.putExtra("userEmail", studentEmail);
            startActivity(intent);
        });

        findViewById(R.id.btnAdminReports).setOnClickListener(v -> {
            Intent intent = new Intent(this, ReportActivity.class);
            intent.putExtra("userEmail", studentEmail);
            startActivity(intent);
        });

        findViewById(R.id.btnAdminEdit).setOnClickListener(v -> {
            Cursor c = database.getReadableDatabase().rawQuery("SELECT * FROM students WHERE email=?", new String[]{studentEmail});
            if (c.moveToFirst()) {
                Intent intent = new Intent(this, AddStudentActivity.class);
                intent.putExtra("id", c.getInt(0));
                intent.putExtra("roll", c.getString(3));
                intent.putExtra("name", c.getString(1));
                intent.putExtra("email", c.getString(2));
                intent.putExtra("course", c.getString(5));
                intent.putExtra("sem", c.getString(6));
                intent.putExtra("phone", c.getString(7));
                startActivity(intent);
                finish(); // Close details view after edit start to ensure refresh on return
            }
            c.close();
        });
    }

    private void initViews() {
        textName = findViewById(R.id.adminStudentName);
        textClass = findViewById(R.id.adminStudentClass);

        setupInfoRow(R.id.rowCourse, android.R.drawable.ic_menu_info_details, "Course");
        setupInfoRow(R.id.rowSem, android.R.drawable.ic_menu_agenda, "Semester");
        setupInfoRow(R.id.rowEmail, android.R.drawable.ic_dialog_email, "Email");
        setupInfoRow(R.id.rowPhone, android.R.drawable.stat_sys_phone_call, "Phone");

        textCourse = findViewById(R.id.rowCourse).findViewById(R.id.infoValue);
        textSem = findViewById(R.id.rowSem).findViewById(R.id.infoValue);
        textEmail = findViewById(R.id.rowEmail).findViewById(R.id.infoValue);
        textPhone = findViewById(R.id.rowPhone).findViewById(R.id.infoValue);
    }

    private void setupInfoRow(int rowId, int iconRes, String label) {
        View row = findViewById(rowId);
        ImageView icon = row.findViewById(R.id.infoIcon);
        TextView tvLabel = row.findViewById(R.id.infoLabel);
        icon.setImageResource(iconRes);
        tvLabel.setText(label);
    }

    private void loadStudentData(String name, String email) {
        textName.setText(name);
        textEmail.setText(email);

        Cursor c = database.getReadableDatabase().rawQuery("SELECT * FROM students WHERE email=?", new String[]{email});
        if (c.moveToFirst()) {
            String roll = c.getString(3);
            textClass.setText(roll + " | Active");
            textCourse.setText(c.getString(5)); // course
            textSem.setText(c.getString(6));   // semester
            textPhone.setText(c.getString(7)); // phone
        }
        c.close();
    }
}