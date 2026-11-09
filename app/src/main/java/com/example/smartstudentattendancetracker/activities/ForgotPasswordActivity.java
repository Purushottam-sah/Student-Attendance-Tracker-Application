package com.example.smartstudentattendancetracker.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartstudentattendancetracker.R;
import com.example.smartstudentattendancetracker.database.SQLiteHelper;

public class ForgotPasswordActivity extends AppCompatActivity {

    private static final String TAG = "ForgotPasswordActivity";
    private LinearLayout layout1, layout3;
    private EditText editEmail, editNewPass;
    private TextView btnBack;
    private Button btnVerifyEmail, btnReset;
    private SQLiteHelper database;
    private String currentEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        database = new SQLiteHelper(this);
        initViews();

        btnVerifyEmail.setOnClickListener(v -> step1());
        btnReset.setOnClickListener(v -> step3());
        btnBack.setOnClickListener(v -> finish());
    }

    private void initViews() {
        layout1 = findViewById(R.id.layoutStep1);
        layout3 = findViewById(R.id.layoutStep3);

        editEmail = findViewById(R.id.forgotEmail);
        editNewPass = findViewById(R.id.newPassword);

        btnBack = findViewById(R.id.forgotBackToLogin);

        btnVerifyEmail = findViewById(R.id.btnVerifyEmail);
        btnReset = findViewById(R.id.btnResetPassword);
    }

    private void step1() {
        String email = editEmail.getText().toString().trim();
        if (email.isEmpty()) {
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }

        currentEmail = email;
        if (!database.checkUserExists(email)) {
            Toast.makeText(this, "Email not found on this device. Setting new password will create your account.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "User verified. Set new password.", Toast.LENGTH_SHORT).show();
        }

        layout1.setVisibility(View.GONE);
        layout3.setVisibility(View.VISIBLE);
    }

    private void step3() {
        String newPass = editNewPass.getText().toString().trim();
        if (newPass.isEmpty() || newPass.length() < 4) {
            Toast.makeText(this, "Password must be at least 4 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        if (database.resetPassword(currentEmail, newPass)) {
            Toast.makeText(this, "Password Set Successfully! You can now login.", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to set password. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
}