package com.example.smartstudentattendancetracker.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstudentattendancetracker.R;

public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.ViewHolder> {

    private final String[] titles = {
            "Welcome to Tracker",
            "Mark Attendance",
            "Smart Notes",
            "Daily Reminders"
    };

    private final String[] descriptions = {
            "The smartest way to manage your academic life and never miss a class.",
            "Mark your attendance with one tap and track your progress in real-time.",
            "Take quick notes for your subjects and keep your study material organized.",
            "Get notified to mark your attendance and stay on top of your goals."
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_onboarding_slide, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(titles[position]);
        holder.desc.setText(descriptions[position]);
        holder.image.setImageResource(R.drawable.ic_logo_color);
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, desc;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textTitle);
            desc = itemView.findViewById(R.id.textDescription);
            image = itemView.findViewById(R.id.imageOnboarding);
        }
    }
}