package com.example.smartstudentattendancetracker.session;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private SharedPreferences preferences;

    public SessionManager(Context context) {
        preferences = context.getSharedPreferences(
                "login_session",
                Context.MODE_PRIVATE);
    }

    public void createSession(String email) {
        preferences.edit()
                .putString("email", email)
                .apply();
    }

    public String getUserEmail() {
        return preferences.getString("email", "");
    }

    public boolean isLogin() {
        return preferences.contains("email");
    }

    public void logout() {
        preferences.edit().clear().apply();
    }
}