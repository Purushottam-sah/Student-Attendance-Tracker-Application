package com.example.smartstudentattendancetracker.activities;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartstudentattendancetracker.R;
import com.example.smartstudentattendancetracker.database.SQLiteHelper;

import java.util.Calendar;
import java.util.Locale;

public class AddTimetableActivity extends AppCompatActivity {

    private AutoCompleteTextView spinnerDay;
    private EditText editSubject, editStart, editEnd;
    private Button btnSave;
    private SQLiteHelper database;
    private String selectedClass;

    private final String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_timetable);

        database = new SQLiteHelper(this);
        selectedClass = getIntent().getStringExtra("className");
        
        initViews();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, days);
        spinnerDay.setAdapter(adapter);

        editStart.setOnClickListener(v -> showTimePicker(editStart));
        editEnd.setOnClickListener(v -> showTimePicker(editEnd));
        btnSave.setOnClickListener(v -> saveSchedule());
    }

    private void initViews() {
        spinnerDay = findViewById(R.id.spinnerDay);
        editSubject = findViewById(R.id.editTimetableSubject);
        editStart = findViewById(R.id.editStartTime);
        editEnd = findViewById(R.id.editEndTime);
        btnSave = findViewById(R.id.btnSaveTimetable);
    }

    private void showTimePicker(EditText target) {
        Calendar c = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            String amPm = (hourOfDay < 12) ? "AM" : "PM";
            int hour = (hourOfDay == 0 || hourOfDay == 12) ? 12 : hourOfDay % 12;
            target.setText(String.format(Locale.getDefault(), "%02d:%02d %s", hour, minute, amPm));
        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
        timePickerDialog.show();
    }

    private void saveSchedule() {
        String day = spinnerDay.getText().toString();
        String subject = editSubject.getText().toString().trim();
        String start = editStart.getText().toString();
        String end = editEnd.getText().toString();

        if (day.isEmpty() || subject.isEmpty() || start.isEmpty() || end.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedClass == null) selectedClass = "BCA-1"; // Default for now

        if (database.addTimetable(day, subject, start, end, selectedClass)) {
            Toast.makeText(this, "Schedule saved", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to save schedule", Toast.LENGTH_SHORT).show();
        }
    }
}