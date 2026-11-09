package com.example.smartstudentattendancetracker.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

public class ThemeManager {

    public static void setDark(Context context, boolean value) {

        SharedPreferences sp = context.getSharedPreferences("theme", Context.MODE_PRIVATE);

        sp.edit().putBoolean("dark", value).apply();

        if (value) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public static boolean isDark(Context context) {

        SharedPreferences sp = context.getSharedPreferences("theme", Context.MODE_PRIVATE);

        return sp.getBoolean("dark", false);
    }
}