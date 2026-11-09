package com.example.smartstudentattendancetracker.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstudentattendancetracker.R;
import com.example.smartstudentattendancetracker.activities.AttendanceActivity;
import com.example.smartstudentattendancetracker.activities.AttendanceHistoryActivity;
import com.example.smartstudentattendancetracker.database.SQLiteHelper;
import com.example.smartstudentattendancetracker.models.Subject;
import com.example.smartstudentattendancetracker.utils.AttendanceUtils;

import java.util.ArrayList;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Subject> list;
    private SQLiteHelper database;

    public SubjectAdapter(Context context, ArrayList<Subject> list, SQLiteHelper database) {
        this.context = context;
        this.list = list;
        this.database = database;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_subject, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Subject subject = list.get(position);

        holder.title.setText(subject.getName());

        int percentage = AttendanceUtils.calculatePercentage(subject.getPresentClass(), subject.getTotalClass());

        holder.info.setText("Attendance : " + percentage + "%");

        holder.history.setOnClickListener(v -> {
            Intent intent = new Intent(context, AttendanceHistoryActivity.class);
            intent.putExtra("subjectId", subject.getId());
            intent.putExtra("subjectName", subject.getName());
            context.startActivity(intent);
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AttendanceActivity.class);
            intent.putExtra("subjectId", subject.getId());
            intent.putExtra("subjectName", subject.getName());
            context.startActivity(intent);
        });

        holder.delete.setOnClickListener(v -> {
            database.deleteSubject(subject.getId());
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                list.remove(currentPosition);
                notifyItemRemoved(currentPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, info;
        Button delete, history;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.subjectTitle);
            info = itemView.findViewById(R.id.attendanceInfo);
            delete = itemView.findViewById(R.id.deleteSubject);
            history = itemView.findViewById(R.id.historySubject);
        }
    }
}