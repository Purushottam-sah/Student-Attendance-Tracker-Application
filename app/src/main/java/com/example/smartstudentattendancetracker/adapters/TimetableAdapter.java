package com.example.smartstudentattendancetracker.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstudentattendancetracker.R;
import com.example.smartstudentattendancetracker.models.Timetable;

import java.util.ArrayList;

public class TimetableAdapter extends RecyclerView.Adapter<TimetableAdapter.ViewHolder> {

    private ArrayList<Timetable> list;
    private OnTimetableDeleteListener listener;

    public interface OnTimetableDeleteListener {
        void onDelete(int id, int position);
    }

    public TimetableAdapter(ArrayList<Timetable> list, OnTimetableDeleteListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timetable, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Timetable timetable = list.get(position);
        holder.subject.setText(timetable.getSubjectName());
        holder.time.setText(timetable.getStartTime());
        
        // Dummy room info for UI overhaul
        holder.room.setText("Room " + (300 + position) + " • Lecture");

        // Random border colors for visual variety
        int[] colors = {R.color.primary_blue, R.color.present_green, R.color.leave_orange, R.color.today_blue};
        holder.colorBorder.setBackgroundResource(colors[position % colors.length]);

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDelete(timetable.getId(), holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView subject, time, room;
        View colorBorder;
        ImageButton btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subject = itemView.findViewById(R.id.timetableSubject);
            time = itemView.findViewById(R.id.timetableTime);
            room = itemView.findViewById(R.id.timetableRoom);
            colorBorder = itemView.findViewById(R.id.colorBorder);
            btnDelete = itemView.findViewById(R.id.btnDeleteTimetable);
        }
    }
}