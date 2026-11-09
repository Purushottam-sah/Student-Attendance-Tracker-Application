package com.example.smartstudentattendancetracker.activities;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.smartstudentattendancetracker.R;
import com.example.smartstudentattendancetracker.database.SQLiteHelper;
import com.example.smartstudentattendancetracker.session.SessionManager;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText editCurrent, editNew, editConfirm;
    private ImageView checkLength, checkNumber, checkSpecial;
    private SQLiteHelper database;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        database = new SQLiteHelper(this);
        session = new SessionManager(this);

        editCurrent = findViewById(R.id.editCurrentPassword);
        editNew = findViewById(R.id.editNewPassword);
        editConfirm = findViewById(R.id.editConfirmPassword);
        
        checkLength = findViewById(R.id.checkLength);
        checkNumber = findViewById(R.id.checkNumber);
        checkSpecial = findViewById(R.id.checkSpecial);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnUpdatePassword).setOnClickListener(v -> updatePassword());

        setupPasswordCriteriaWatcher();
    }

    private void setupPasswordCriteriaWatcher() {
        editNew.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = s.toString();
                
                boolean isLengthOk = password.length() >= 8;
                boolean hasNumber = password.matches(".*\\d.*");
                boolean hasSpecial = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");

                updateCriteriaIcon(checkLength, isLengthOk);
                updateCriteriaIcon(checkNumber, hasNumber);
                updateCriteriaIcon(checkSpecial, hasSpecial);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void updateCriteriaIcon(ImageView icon, boolean isValid) {
        int color = isValid ? R.color.present_green : R.color.text_secondary;
        icon.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(this, color)));
    }

    private void updatePassword() {
        String current = editCurrent.getText().toString();
        String newPass = editNew.getText().toString();
        String confirm = editConfirm.getText().toString();
        String email = session.getUserEmail();

        if (current.isEmpty() || newPass.isEmpty() || confirm.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPass.equals(confirm)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (newPass.length() < 8) {
            Toast.makeText(this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        if (database.loginUser(email, current)) {
            if (database.resetPassword(email, newPass)) {
                Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to update password", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Incorrect current password", Toast.LENGTH_SHORT).show();
        }
    }
}