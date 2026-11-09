package com.example.smartstudentattendancetracker.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstudentattendancetracker.R;
import com.example.smartstudentattendancetracker.models.Notice;

import java.util.ArrayList;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {

    private ArrayList<Notice> list;

    public NoticeAdapter(ArrayList<Notice> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notice, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notice notice = list.get(position);
        holder.title.setText(notice.getTitle());
        holder.message.setText(notice.getMessage());
        
        // Format date and category if available, otherwise just date
        holder.date.setText(notice.getDate());
        
        // Show "New" badge for the first two items as a demo
        if (position < 2) {
            holder.badgeNew.setVisibility(View.VISIBLE);
        } else {
            holder.badgeNew.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, message, date, badgeNew;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textNoticeTitle);
            message = itemView.findViewById(R.id.textNoticeMessage);
            date = itemView.findViewById(R.id.textNoticeDate);
            badgeNew = itemView.findViewById(R.id.badgeNew);
        }
    }
}