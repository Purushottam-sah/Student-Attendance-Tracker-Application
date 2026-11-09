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

public class SignupActivity extends AppCompatActivity {

    private EditText name, email, password;
    private AutoCompleteTextView spinnerCourse, spinnerSem;
    private Button signup;
    private TextView loginLink;
    private SQLiteHelper db;

    private final String[] courses = {"MCA", "BCA", "B.Tech", "MBA", "M.Sc. IT"};
    private final String[] semesters = {"Sem 1", "Sem 2", "Sem 3", "Sem 4", "Sem 5", "Sem 6"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        db = new SQLiteHelper(this);
        initViews();
        setupAdapters();

        signup.setOnClickListener(v -> handleSignup());
        loginLink.setOnClickListener(v -> finish());
    }

    private void initViews() {
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        spinnerCourse = findViewById(R.id.signupCourse);
        spinnerSem = findViewById(R.id.signupSemester);
        signup = findViewById(R.id.signupBtn);
        loginLink = findViewById(R.id.loginText);
    }

    private void setupAdapters() {
        ArrayAdapter<String> cAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, courses);
        spinnerCourse.setAdapter(cAdapter);

        ArrayAdapter<String> sAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, semesters);
        spinnerSem.setAdapter(sAdapter);
    }

    private void handleSignup() {
        String nameStr = name.getText().toString().trim();
        String emailStr = email.getText().toString().trim();
        String passStr = password.getText().toString().trim();
        String courseStr = spinnerCourse.getText().toString();
        String semStr = spinnerSem.getText().toString();

        String className;
        if (courseStr.isEmpty() && semStr.isEmpty()) {
            className = "Unassigned";
        } else if (courseStr.isEmpty()) {
            className = semStr;
        } else if (semStr.isEmpty()) {
            className = courseStr;
        } else {
            className = courseStr + " - " + semStr;
        }

        // Passing empty strings for security question/answer as requested
        if (db.registerUser(nameStr, emailStr, passStr, "", "")) {
            String role = db.getUserRole(emailStr);
            if ("STUDENT".equals(role)) {
                db.addStudent(nameStr, emailStr, "ROLL-TBD", className);
                db.updateProfile(emailStr, "Institutional College", courseStr, semStr, "0000000000", "01-01-2000", "City, State", "TBD");
            }
            Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Registration Failed. Email might be taken.", Toast.LENGTH_SHORT).show();
        }
    }
}