package com.example.smartstudentattendancetracker.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartstudentattendancetracker.R;
import com.example.smartstudentattendancetracker.database.SQLiteHelper;
import com.example.smartstudentattendancetracker.session.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddNoticeActivity extends AppCompatActivity {

    private EditText editTitle, editMessage;
    private Spinner spinnerCategory;
    private Button btnPost;
    private SQLiteHelper database;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notice);

        database = new SQLiteHelper(this);
        session = new SessionManager(this);

        editTitle = findViewById(R.id.editNoticeTitle);
        editMessage = findViewById(R.id.editNoticeMessage);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnPost = findViewById(R.id.btnPostNotice);

        setupCategorySpinner();

        btnPost.setOnClickListener(v -> postNotice());
    }

    private void setupCategorySpinner() {
        String[] categories = {"General", "Exam", "Attendance", "Event", "Holiday"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    }

    private void postNotice() {
        String title = editTitle.getText().toString().trim();
        String message = editMessage.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String postedBy = database.getUserName(session.getUserEmail());

        if (title.isEmpty() || message.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (database.addNotice(title, message, date, postedBy, category)) {
            Toast.makeText(this, "Notice posted successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to post notice", Toast.LENGTH_SHORT).show();
        }
    }
}