package com.example.smartstudentattendancetracker.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartstudentattendancetracker.R;
import com.example.smartstudentattendancetracker.database.SQLiteHelper;
import com.example.smartstudentattendancetracker.session.SessionManager;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIME = 2500;
    private static final String TAG = "SplashActivity";
    private boolean isNavigating = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logo = findViewById(R.id.logo);
        if (logo != null) {
            try {
                Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
                logo.startAnimation(animation);
            } catch (Exception ignored) {}
        }

        new Handler(Looper.getMainLooper()).postDelayed(this::navigateToNext, SPLASH_TIME);
    }

    private synchronized void navigateToNext() {
        if (isFinishing() || isNavigating) return;
        isNavigating = true;
        
        try {
            SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
            boolean isFirstLaunch = prefs.getBoolean("is_first_launch", true);

            Intent intent;
            if (isFirstLaunch) {
                intent = new Intent(SplashActivity.this, OnboardingActivity.class);
            } else {
                SessionManager session = new SessionManager(this);
                if (session.isLogin()) {
                    SQLiteHelper db = new SQLiteHelper(this);
                    String role = db.getUserRole(session.getUserEmail());
                    if ("ADMIN".equals(role)) {
                        intent = new Intent(SplashActivity.this, AdminDashboardActivity.class);
                    } else {
                        intent = new Intent(SplashActivity.this, MainActivity.class);
                    }
                } else {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                }
            }
            
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            
        } catch (Exception e) {
            Log.e(TAG, "Navigation error", e);
            // Fallback to Login
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }
}