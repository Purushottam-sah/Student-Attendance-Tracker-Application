package com.example.smartstudentattendancetracker.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartstudentattendancetracker.R;
import com.example.smartstudentattendancetracker.database.SQLiteHelper;
import com.example.smartstudentattendancetracker.session.SessionManager;

public class AddSubjectActivity extends AppCompatActivity {

    private EditText subjectName;
    private Button save;
    private SQLiteHelper database;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);

        database = new SQLiteHelper(this);
        session = new SessionManager(this);
        initViews();

        save.setOnClickListener(v -> {
            String name = subjectName.getText().toString().trim();
            String userEmail = getIntent().getStringExtra("userEmail"); // Passed from Admin flow
            
            if (userEmail == null) {
                userEmail = session.getUserEmail(); // Default to self if not provided
            }

            if (!name.isEmpty()) {
                database.addSubject(name, userEmail);
                Toast.makeText(this, "Subject Added", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Please enter subject name", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews() {
        subjectName = findViewById(R.id.subjectName);
        save = findViewById(R.id.saveSubject);
    }
}