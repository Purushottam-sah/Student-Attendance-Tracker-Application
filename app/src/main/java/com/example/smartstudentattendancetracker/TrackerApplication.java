package com.example.smartstudentattendancetracker;

import android.app.Application;
import android.util.Log;
import androidx.appcompat.app.AppCompatDelegate;
import com.example.smartstudentattendancetracker.utils.ThemeManager;

public class TrackerApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            // Initialize Theme from SharedPreferences on start
            if (ThemeManager.isDark(this)) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        } catch (Exception e) {
            Log.e("TrackerApplication", "Error setting initial theme", e);
        }
    }
}