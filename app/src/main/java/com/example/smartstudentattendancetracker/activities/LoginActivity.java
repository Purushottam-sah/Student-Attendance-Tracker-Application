package com.example.smartstudentattendancetracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartstudentattendancetracker.R;
import com.example.smartstudentattendancetracker.database.SQLiteHelper;
import com.example.smartstudentattendancetracker.session.SessionManager;
import com.google.android.material.button.MaterialButtonToggleGroup;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private Button login;
    private TextView signup, forgotPassword, welcomeText;
    private ImageView loginIllustration;
    private MaterialButtonToggleGroup roleToggle;
    private com.google.android.material.checkbox.MaterialCheckBox rememberMe;

    private SQLiteHelper db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.loginBtn);
        signup = findViewById(R.id.signupText);
        forgotPassword = findViewById(R.id.forgotPasswordLink);
        roleToggle = findViewById(R.id.roleToggleGroup);
        rememberMe = findViewById(R.id.rememberMe);
        welcomeText = findViewById(R.id.welcomeText);
        loginIllustration = findViewById(R.id.loginIllustration);

        db = new SQLiteHelper(this);
        session = new SessionManager(this);

        // Remember Me auto-fill
        android.content.SharedPreferences sp = getSharedPreferences("remember_me", MODE_PRIVATE);
        if (sp.getBoolean("checked", false)) {
            email.setText(sp.getString("user", ""));
            password.setText(sp.getString("pass", ""));
            rememberMe.setChecked(true);
        }

        roleToggle.check(R.id.btnRoleStudent);

        roleToggle.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                if (checkedId == R.id.btnRoleAdmin) {
                    welcomeText.setText("Welcome Admin! 👋");
                    loginIllustration.setImageResource(R.drawable.ic_admin_login);
                    signup.setVisibility(View.GONE);
                } else {
                    welcomeText.setText("Welcome Back! 👋");
                    loginIllustration.setImageResource(R.drawable.ic_student_login);
                    signup.setVisibility(View.VISIBLE);
                }
            }
        });

        login.setOnClickListener(v -> {
            String emailStr = email.getText().toString().trim();
            String passStr = password.getText().toString().trim();

            if (emailStr.isEmpty() || passStr.isEmpty()) {
                Toast.makeText(this, "Please enter your credentials", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean isAdminMode = roleToggle.getCheckedButtonId() == R.id.btnRoleAdmin;

            if (db.loginUser(emailStr, passStr)) {
                String role = db.getUserRole(emailStr);

                if (isAdminMode && !"ADMIN".equals(role)) {
                    Toast.makeText(this, "This account does not have Admin access", Toast.LENGTH_SHORT).show();
                    return;
                }

                session.createSession(emailStr);
                saveRememberMe(emailStr, passStr);

                Intent intent = "ADMIN".equals(role)
                        ? new Intent(LoginActivity.this, AdminDashboardActivity.class)
                        : new Intent(LoginActivity.this, MainActivity.class);
                
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {
                // If account does not exist on this device yet and in Student mode, auto-register & login
                if (!isAdminMode && !db.checkUserExists(emailStr)) {
                    String name = emailStr.contains("@") ? emailStr.split("@")[0] : emailStr;
                    if (db.autoRegisterOrLoginStudent(name, emailStr, passStr)) {
                        session.createSession(emailStr);
                        saveRememberMe(emailStr, passStr);
                        Toast.makeText(this, "Account created & logged in!", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                        return;
                    }
                }
                
                if (db.checkUserExists(emailStr)) {
                    Toast.makeText(this, "Incorrect Password. Tap Forgot Password to reset.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Account not found on this device. Tap Sign Up or Forgot Password.", Toast.LENGTH_LONG).show();
                }
            }
        });

        signup.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        });

        forgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
        });
    }

    private void saveRememberMe(String emailStr, String passStr) {
        android.content.SharedPreferences.Editor editor = getSharedPreferences("remember_me", MODE_PRIVATE).edit();
        if (rememberMe != null && rememberMe.isChecked()) {
            editor.putBoolean("checked", true);
            editor.putString("user", emailStr);
            editor.putString("pass", passStr);
        } else {
            editor.clear();
        }
        editor.apply();
    }
}