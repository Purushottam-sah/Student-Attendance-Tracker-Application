package com.example.smartstudentattendancetracker.activities;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.smartstudentattendancetracker.R;

public class TermsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legal_text);

        Toolbar toolbar = findViewById(R.id.toolbarLegal);
        toolbar.setTitle("Terms & Conditions");
        toolbar.setNavigationOnClickListener(v -> finish());

        TextView content = findViewById(R.id.textLegalContent);
        content.setText("Terms & Conditions\n\n" +
                "1. Acceptance: By using this app, you agree to institutional terms.\n" +
                "2. Responsibility: Admin accounts are responsible for marking accurate data.\n" +
                "3. Usage: This app is strictly for educational institutional use.\n" +
                "4. Disclaimer: Data is stored locally; device loss may result in data loss.");
    }
}