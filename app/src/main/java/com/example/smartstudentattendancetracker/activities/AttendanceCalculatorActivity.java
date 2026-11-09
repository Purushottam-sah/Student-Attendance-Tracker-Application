package com.example.smartstudentattendancetracker.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartstudentattendancetracker.R;
import com.example.smartstudentattendancetracker.utils.AttendanceUtils;
import com.google.android.material.card.MaterialCardView;

public class AttendanceCalculatorActivity extends AppCompatActivity {

    private EditText inputPresent, inputTotal, inputGoal;
    private Button btnCalculate;
    private MaterialCardView resultCard;
    private TextView resultPercentage, resultAction, resultDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_calculator);

        inputPresent = findViewById(R.id.inputPresent);
        inputTotal = findViewById(R.id.inputTotal);
        inputGoal = findViewById(R.id.inputGoal);
        btnCalculate = findViewById(R.id.btnCalculate);
        resultCard = findViewById(R.id.resultCard);
        resultPercentage = findViewById(R.id.resultPercentage);
        resultAction = findViewById(R.id.resultAction);
        resultDetail = findViewById(R.id.resultDetail);

        btnCalculate.setOnClickListener(v -> calculate());
    }

    private void calculate() {
        String presentStr = inputPresent.getText().toString();
        String totalStr = inputTotal.getText().toString();
        String goalStr = inputGoal.getText().toString();

        if (presentStr.isEmpty() || totalStr.isEmpty() || goalStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int present = Integer.parseInt(presentStr);
        int total = Integer.parseInt(totalStr);
        int goal = Integer.parseInt(goalStr);

        if (present > total) {
            Toast.makeText(this, "Present classes cannot be more than total", Toast.LENGTH_SHORT).show();
            return;
        }

        int currentPercent = AttendanceUtils.calculatePercentage(present, total);
        resultPercentage.setText(currentPercent + "%");
        resultCard.setVisibility(View.VISIBLE);

        if (currentPercent < goal) {
            int needed = AttendanceUtils.classesNeeded(present, total, goal);
            resultAction.setText("Action Required");
            resultAction.setTextColor(getResources().getColor(R.color.red, getTheme()));
            resultDetail.setText("You need to attend " + needed + " more classes without missing to reach your " + goal + "% goal.");
            resultPercentage.setTextColor(getResources().getColor(R.color.red, getTheme()));
        } else {
            int canMiss = AttendanceUtils.classesCanMiss(present, total, goal);
            resultAction.setText("Attendance Safe");
            resultAction.setTextColor(getResources().getColor(R.color.green, getTheme()));
            if (canMiss > 0) {
                resultDetail.setText("You can safely miss " + canMiss + " more classes while staying above your " + goal + "% threshold.");
            } else {
                resultDetail.setText("You are exactly at the threshold. Don't miss any more classes!");
            }
            resultPercentage.setTextColor(getResources().getColor(R.color.green, getTheme()));
        }
    }
}