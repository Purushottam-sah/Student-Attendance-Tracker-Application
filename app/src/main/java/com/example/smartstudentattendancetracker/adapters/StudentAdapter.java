package com.example.smartstudentattendancetracker.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstudentattendancetracker.R;
import com.example.smartstudentattendancetracker.models.Student;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {

    private ArrayList<Student> list;
    private OnStudentActionListener listener;

    public interface OnStudentActionListener {
        void onEdit(Student student);
        void onDelete(int id, int position);
        void onManage(Student student);
    }

    public StudentAdapter(ArrayList<Student> list, OnStudentActionListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Student student = list.get(position);
        holder.name.setText(student.getName());
        holder.info.setText("Roll: " + student.getRollNumber() + " | Class: " + student.getClassName());

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(student);
        });

        holder.btnManage.setOnClickListener(v -> {
            if (listener != null) listener.onManage(student);
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(student.getId(), holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, info;
        ImageButton btnEdit, btnDelete, btnManage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textStudentName);
            info = itemView.findViewById(R.id.textStudentRoll);
            btnEdit = itemView.findViewById(R.id.btnEditStudent);
            btnDelete = itemView.findViewById(R.id.btnDeleteStudent);
            btnManage = itemView.findViewById(R.id.btnManageStudent);
        }
    }
}