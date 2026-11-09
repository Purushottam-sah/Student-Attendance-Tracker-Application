package com.example.smartstudentattendancetracker.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartstudentattendancetracker.R;
import com.example.smartstudentattendancetracker.database.SQLiteHelper;
import com.example.smartstudentattendancetracker.session.SessionManager;

public class EditProfileActivity extends AppCompatActivity {

    private EditText editName, editPhone, editDOB, editAddress;
    private Button btnSave;
    private SQLiteHelper database;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        database = new SQLiteHelper(this);
        session = new SessionManager(this);

        editName = findViewById(R.id.editName);
        editPhone = findViewById(R.id.editPhone);
        editDOB = findViewById(R.id.editDOB);
        editAddress = findViewById(R.id.editAddress);
        btnSave = findViewById(R.id.btnSaveProfile);

        loadCurrentData();

        btnSave.setOnClickListener(v -> saveProfile());
    }

    private void loadCurrentData() {
        String email = session.getUserEmail();
        editName.setText(database.getUserName(email));

        Cursor c = database.getProfile(email);
        if (c.moveToFirst()) {
            editPhone.setText(c.getString(5));
            editDOB.setText(c.getString(6));
            editAddress.setText(c.getString(7));
        }
        c.close();
    }

    private void saveProfile() {
        String name = editName.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();
        String dob = editDOB.getText().toString().trim();
        String address = editAddress.getText().toString().trim();
        String email = session.getUserEmail();

        if (name.isEmpty()) {
            Toast.makeText(this, "Name is required", Toast.LENGTH_SHORT).show();
            return;
        }

        // We need to keep old course/semester
        String college = "", course = "", sem = "", studentId = "";
        Cursor c = database.getProfile(email);
        if (c.moveToFirst()) {
            college = c.getString(2);
            course = c.getString(3);
            sem = c.getString(4);
            studentId = c.getString(8);
        }
        c.close();

        database.updateUserName(email, name);
        if (database.updateProfile(email, college, course, sem, phone, dob, address, studentId)) {
            Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to update", Toast.LENGTH_SHORT).show();
        }
    }
}