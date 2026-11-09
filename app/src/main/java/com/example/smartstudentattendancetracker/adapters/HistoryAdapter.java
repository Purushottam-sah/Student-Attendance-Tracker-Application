package com.example.smartstudentattendancetracker.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstudentattendancetracker.R;
import com.example.smartstudentattendancetracker.models.Attendance;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private final Context context;
    private ArrayList<Attendance> list;
    private OnHistoryDeleteListener listener;

    public interface OnHistoryDeleteListener {
        void onDelete(Attendance attendance, int position);
    }

    public HistoryAdapter(Context context, ArrayList<Attendance> list, OnHistoryDeleteListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    public void filterList(ArrayList<Attendance> filteredList) {
        this.list = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_history, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Attendance attendance = list.get(position);

        holder.subjectName.setText(attendance.getSubjectName());
        holder.dateText.setText(attendance.getDate());
        holder.statusText.setText(attendance.getStatus());

        if (attendance.getStatus().equalsIgnoreCase("Present")) {
            holder.statusDot.setBackgroundResource(R.drawable.dot_present);
            holder.statusText.setTextColor(context.getResources().getColor(R.color.present_green));
        } else if (attendance.getStatus().equalsIgnoreCase("Absent")) {
            holder.statusDot.setBackgroundResource(R.drawable.dot_absent);
            holder.statusText.setTextColor(context.getResources().getColor(R.color.absent_red));
        } else if (attendance.getStatus().equalsIgnoreCase("Leave")) {
            holder.statusDot.setBackgroundResource(R.drawable.dot_leave);
            holder.statusText.setTextColor(context.getResources().getColor(R.color.leave_orange));
        }

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDelete(attendance, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView subjectName, dateText, statusText;
        View statusDot;
        ImageButton btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectName = itemView.findViewById(R.id.historySubjectName);
            dateText = itemView.findViewById(R.id.historyDate);
            statusText = itemView.findViewById(R.id.historyStatus);
            statusDot = itemView.findViewById(R.id.statusDot);
            btnDelete = itemView.findViewById(R.id.btnDeleteHistory);
        }
    }
}