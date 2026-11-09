package com.example.smartstudentattendancetracker.activities;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.smartstudentattendancetracker.R;

public class PrivacyActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legal_text);

        Toolbar toolbar = findViewById(R.id.toolbarLegal);
        toolbar.setTitle("Privacy Policy");
        toolbar.setNavigationOnClickListener(v -> finish());

        TextView content = findViewById(R.id.textLegalContent);
        content.setText("Privacy Policy\n\n" +
                "1. Data Collection: We collect minimum student data for attendance tracking.\n" +
                "2. Data Usage: Your data is stored locally and used only for academic monitoring.\n" +
                "3. Security: We use offline SQLite encryption where possible.\n" +
                "4. Access: Only authorized Admins can view and mark attendance.");
    }
}