package com.example.smartstudentattendancetracker.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstudentattendancetracker.R;
import com.example.smartstudentattendancetracker.models.Notification;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private List<Notification> notifications;

    public NotificationAdapter(List<Notification> notifications) {
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notification notification = notifications.get(position);
        holder.title.setText(notification.getTitle());
        holder.message.setText(notification.getMessage());
        holder.time.setText(notification.getTime());
        holder.statusDot.setVisibility(notification.isRead() ? View.GONE : View.VISIBLE);

        int iconRes;
        switch (notification.getType()) {
            case 0: iconRes = android.R.drawable.checkbox_on_background; break;
            case 1: iconRes = android.R.drawable.ic_dialog_alert; break;
            case 2: iconRes = android.R.drawable.ic_menu_agenda; break;
            case 3: iconRes = android.R.drawable.ic_menu_today; break;
            default: iconRes = android.R.drawable.ic_dialog_info;
        }
        holder.icon.setImageResource(iconRes);
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, message, time;
        ImageView icon;
        View statusDot;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.notiTitle);
            message = itemView.findViewById(R.id.notiMessage);
            time = itemView.findViewById(R.id.notiTime);
            icon = itemView.findViewById(R.id.notiIcon);
            statusDot = itemView.findViewById(R.id.notiStatusDot);
        }
    }
}