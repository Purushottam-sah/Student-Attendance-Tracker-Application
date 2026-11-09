package com.example.smartstudentattendancetracker.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstudentattendancetracker.R;
import com.example.smartstudentattendancetracker.models.Student;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BatchAttendanceAdapter extends RecyclerView.Adapter<BatchAttendanceAdapter.ViewHolder> {

    private ArrayList<Student> list;
    private Map<Integer, String> attendanceMap;

    public BatchAttendanceAdapter(ArrayList<Student> list) {
        this.list = list;
        this.attendanceMap = new HashMap<>();
        // Initialize all as Present by default
        for (int i = 0; i < list.size(); i++) {
            attendanceMap.put(i, "Present");
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_batch_attendance, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Student student = list.get(position);
        holder.name.setText(student.getName());
        holder.roll.setText("Roll: " + student.getRollNumber());

        holder.toggleGroup.check(R.id.btnPresent); // Default

        holder.toggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                if (checkedId == R.id.btnPresent) attendanceMap.put(position, "Present");
                else if (checkedId == R.id.btnAbsent) attendanceMap.put(position, "Absent");
                else if (checkedId == R.id.btnLate) attendanceMap.put(position, "Late");
                else if (checkedId == R.id.btnLeave) attendanceMap.put(position, "Leave");
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public String getAttendanceStatus(int position) {
        return attendanceMap.get(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, roll;
        MaterialButtonToggleGroup toggleGroup;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.studentNameBatch);
            roll = itemView.findViewById(R.id.studentRollBatch);
            toggleGroup = itemView.findViewById(R.id.toggleGroupAttendance);
        }
    }
}