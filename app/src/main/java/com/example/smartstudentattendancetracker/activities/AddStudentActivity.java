package com.example.smartstudentattendancetracker.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartstudentattendancetracker.R;
import com.example.smartstudentattendancetracker.database.SQLiteHelper;

public class AddStudentActivity extends AppCompatActivity {

    private EditText editName, editEmail, editRoll, editPhone, editPassword;
    private AutoCompleteTextView spinnerCourse, spinnerSem;
    private Button btnSave;
    private TextView textTitle;
    private SQLiteHelper database;
    private int studentId = -1;

    private final String[] courses = {"BCA", "BBA", "B.Tech", "MCA", "M.Sc. IT"};
    private final String[] semesters = {"Sem 1", "Sem 2", "Sem 3", "Sem 4", "Sem 5", "Sem 6"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        database = new SQLiteHelper(this);
        initViews();
        setupSpinners();

        if (getIntent().hasExtra("id")) {
            studentId = getIntent().getIntExtra("id", -1);
            textTitle.setText("Edit Student Details");
            btnSave.setText("Update Details");
            
            editRoll.setText(getIntent().getStringExtra("roll"));
            editName.setText(getIntent().getStringExtra("name"));
            editEmail.setText(getIntent().getStringExtra("email"));
            editPhone.setText(getIntent().getStringExtra("phone"));
            spinnerCourse.setText(getIntent().getStringExtra("course"), false);
            spinnerSem.setText(getIntent().getStringExtra("sem"), false);
        }

        btnSave.setOnClickListener(v -> saveStudent());
    }

    private void initViews() {
        textTitle = findViewById(R.id.textAddStudentTitle);
        editRoll = findViewById(R.id.editStudentRoll);
        editName = findViewById(R.id.editStudentName);
        editEmail = findViewById(R.id.editStudentEmail);
        editPhone = findViewById(R.id.editStudentPhone);
        editPassword = findViewById(R.id.editStudentPassword);
        
        spinnerCourse = findViewById(R.id.spinnerCourse);
        spinnerSem = findViewById(R.id.spinnerSemester);
        
        btnSave = findViewById(R.id.btnSaveStudent);
    }

    private void setupSpinners() {
        ArrayAdapter<String> cAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, courses);
        spinnerCourse.setAdapter(cAdapter);

        ArrayAdapter<String> sAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, semesters);
        spinnerSem.setAdapter(sAdapter);
    }

    private void saveStudent() {
        String roll = editRoll.getText().toString().trim();
        String name = editName.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();
        String pass = editPassword.getText().toString().trim();
        String course = spinnerCourse.getText().toString();
        String sem = spinnerSem.getText().toString();

        if (name.isEmpty() || email.isEmpty() || roll.isEmpty()) {
            Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String className;
        if (course.isEmpty() && sem.isEmpty()) {
            className = "Unassigned";
        } else if (course.isEmpty()) {
            className = sem;
        } else if (sem.isEmpty()) {
            className = course;
        } else {
            className = course + " - " + sem;
        }

        if (studentId == -1) {
            // New Student
            if (pass.isEmpty()) pass = "student123"; // Default
            if (database.addStudent(name, email, roll, className)) {
                Toast.makeText(this, "Student registered", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Update
            if (database.updateStudent(studentId, name, roll, className)) {
                Toast.makeText(this, "Student updated", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}