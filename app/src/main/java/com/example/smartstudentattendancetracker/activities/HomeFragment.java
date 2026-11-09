package com.example.smartstudentattendancetracker.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.smartstudentattendancetracker.R;
import com.example.smartstudentattendancetracker.database.SQLiteHelper;
import com.example.smartstudentattendancetracker.notifications.NotificationHelper;
import com.example.smartstudentattendancetracker.session.SessionManager;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private TextView welcomeText, textSemester, presentClass, absentClass, leaveCount, percentage, todayStatusText;
    private View actionAttendance, actionAnalytics, actionNotice, actionTimetable, actionProfile, actionNotifications;
    private CircularProgressIndicator dashboardProgress;
    private LineChart miniLineChart;
    private LinearLayout notificationList;

    private SQLiteHelper database;
    private SessionManager session;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_dashboard, container, false);

        initViews(view);
        
        database = new SQLiteHelper(requireContext());
        session = new SessionManager(requireContext());

        setupQuickActions();
        setupClickListeners(view);

        return view;
    }

    private void initViews(View view) {
        welcomeText = view.findViewById(R.id.welcomeText);
        textSemester = view.findViewById(R.id.textSemester);
        presentClass = view.findViewById(R.id.presentClass);
        absentClass = view.findViewById(R.id.absentClass);
        leaveCount = view.findViewById(R.id.leaveCount);
        todayStatusText = view.findViewById(R.id.todayStatus);
        percentage = view.findViewById(R.id.percentage);
        
        actionAttendance = view.findViewById(R.id.actionAttendance);
        actionAnalytics = view.findViewById(R.id.actionAnalytics);
        actionNotice = view.findViewById(R.id.actionNotice);
        actionTimetable = view.findViewById(R.id.actionTimetable);
        actionProfile = view.findViewById(R.id.actionProfile);
        actionNotifications = view.findViewById(R.id.actionNotifications);
        
        dashboardProgress = view.findViewById(R.id.dashboardProgress);
        miniLineChart = view.findViewById(R.id.miniLineChart);
        notificationList = view.findViewById(R.id.notificationList);
    }

    private void setupQuickActions() {
        setupAction(actionAttendance, "Attendance", R.drawable.ic_attendance);
        setupAction(actionAnalytics, "Analytics", R.drawable.ic_analytics);
        setupAction(actionNotice, "Notices", R.drawable.ic_notice);
        setupAction(actionTimetable, "Timetable", R.drawable.ic_timetable);
        setupAction(actionProfile, "Profile", R.drawable.ic_profile);
        setupAction(actionNotifications, "Alerts", R.drawable.ic_notifications);
    }

    private void setupAction(View view, String text, int iconRes) {
        if (view == null) return;
        TextView tv = view.findViewById(R.id.actionText);
        ImageView iv = view.findViewById(R.id.actionIcon);
        if (tv != null) tv.setText(text);
        if (iv != null) iv.setImageResource(iconRes);
    }

    private void setupClickListeners(View root) {
        actionAttendance.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) ((MainActivity)getActivity()).loadFragment(new SubjectFragment());
        });
        actionAnalytics.setOnClickListener(v -> startActivity(new Intent(getActivity(), ReportActivity.class)));
        actionNotice.setOnClickListener(v -> startActivity(new Intent(getActivity(), NoticeActivity.class)));
        actionTimetable.setOnClickListener(v -> startActivity(new Intent(getActivity(), TimetableActivity.class)));
        actionProfile.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) ((MainActivity)getActivity()).loadFragment(new ProfileFragment());
        });
        actionNotifications.setOnClickListener(v -> startActivity(new Intent(getActivity(), NotificationListActivity.class)));

        View profileImg = root.findViewById(R.id.profileImageCard);
        if (profileImg != null) profileImg.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) ((MainActivity)getActivity()).loadFragment(new ProfileFragment());
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isAdded()) {
            updateGreeting();
            updateAttendance();
            initMiniChart();
            updateNotifications();
            checkNewAttendanceNotifications();
            loadProfileImage();
        }
    }

    private void loadProfileImage() {
        if (!isAdded() || getView() == null) return;
        String email = session.getUserEmail();
        Cursor c = null;
        try {
            c = database.getProfile(email);
            if (c != null && c.moveToFirst()) {
                String path = c.getString(10); 
                if (path != null && !path.isEmpty()) {
                    java.io.File file = new java.io.File(path);
                    if (file.exists()) {
                        ImageView iv = getView().findViewById(R.id.profileImage);
                        if (iv != null) iv.setImageURI(android.net.Uri.fromFile(file));
                    }
                }
            }
        } finally {
            if (c != null) c.close();
        }
    }

    private void checkNewAttendanceNotifications() {
        if (database == null || session == null || !isAdded()) return;
        String email = session.getUserEmail();
        Cursor cursor = null;
        try {
            cursor = database.getUnreadNotifications(email);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    String title = cursor.getString(1);
                    String message = cursor.getString(2);
                    NotificationHelper.showAttendanceMarkedNotification(requireContext(), title, message);
                    database.markNotificationRead(id);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) cursor.close();
        }
    }

    private void updateGreeting() {
        String email = session.getUserEmail();
        Calendar c = Calendar.getInstance();
        int hr = c.get(Calendar.HOUR_OF_DAY);
        String greeting = "Good Morning! ☀️";
        if (hr >= 12 && hr < 17) greeting = "Good Afternoon! ☁️";
        else if (hr >= 17 && hr < 21) greeting = "Good Evening! 🌙";
        else if (hr >= 21 || hr < 5) greeting = "Good Night! ✨";

        if (welcomeText != null) welcomeText.setText(greeting);

        Cursor pc = null;
        try {
            pc = database.getProfile(email);
            if (pc != null && pc.moveToFirst()) {
                String sem = pc.getString(4);
                String course = pc.getString(3);
                if (sem == null) sem = "";
                if (course == null) course = "";
                if (textSemester != null) {
                    textSemester.setText(sem.isEmpty() ? "Academic Dashboard" : (sem + " | " + course));
                }
            }
        } catch (Exception e) {
            android.util.Log.e("HomeFragment", "Error updating greeting", e);
        } finally {
            if (pc != null) pc.close();
        }
    }

    private void updateAttendance() {
        if (database == null || session == null || !isAdded() || getView() == null) return;

        String email = session.getUserEmail();
        int present = database.getAllPresentCount(email);
        int leave = database.getAllLeaveCount(email);
        int totalRecords = database.getAllTotalAttendanceCount(email);
        int absent = totalRecords - present - leave;

        presentClass.setText(String.valueOf(present));
        absentClass.setText(String.valueOf(absent));
        leaveCount.setText(String.valueOf(leave));

        final int finalPercent;
        if (totalRecords > 0) {
            finalPercent = database.getOverallPercentage(email);
            percentage.setText(finalPercent + "%");
            dashboardProgress.setProgress(finalPercent, true);
        } else {
            finalPercent = 0;
            percentage.setText("0%");
            dashboardProgress.setProgress(0);
        }

        View alertCard = getView().findViewById(R.id.alertCard);
        if (totalRecords > 0 && finalPercent < 75) {
            percentage.setTextColor(getResources().getColor(R.color.absent_red, getActivity().getTheme()));
            if (alertCard != null) {
                alertCard.setVisibility(View.VISIBLE);
                alertCard.setOnClickListener(v -> startActivity(new Intent(getActivity(), LowAttendanceAlertActivity.class).putExtra("percent", finalPercent)));
            }
        } else {
            percentage.setTextColor(getResources().getColor(R.color.white, getActivity().getTheme()));
            if (alertCard != null) alertCard.setVisibility(View.GONE);
        }
        
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Cursor cursor = null;
        try {
            cursor = database.getReadableDatabase().rawQuery("SELECT status FROM attendance WHERE studentEmail=? AND date=?", new String[]{email, today});
            if (cursor != null && cursor.moveToFirst()) {
                todayStatusText.setText(cursor.getString(0));
            } else {
                todayStatusText.setText("Pending");
            }
        } finally {
            if (cursor != null) cursor.close();
        }
    }

    private void initMiniChart() {
        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, 60));
        entries.add(new Entry(1, 65));
        entries.add(new Entry(2, 62));
        entries.add(new Entry(3, 70));
        entries.add(new Entry(4, 75));
        entries.add(new Entry(5, 82));

        LineDataSet set = new LineDataSet(entries, "");
        set.setColor(android.graphics.Color.WHITE);
        set.setLineWidth(2f);
        set.setDrawCircles(false);
        set.setDrawValues(false);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        LineData data = new LineData(set);
        miniLineChart.setData(data);
        miniLineChart.getLegend().setEnabled(false);
        miniLineChart.getDescription().setEnabled(false);
        miniLineChart.getAxisLeft().setEnabled(false);
        miniLineChart.getAxisRight().setEnabled(false);
        miniLineChart.getXAxis().setEnabled(false);
        miniLineChart.setTouchEnabled(false);
        miniLineChart.invalidate();
    }

    private void updateNotifications() {
        if (notificationList == null || !isAdded()) return;
        notificationList.removeAllViews();
        Cursor cursor = null;
        try {
            cursor = database.getNotices();
            if (cursor != null && cursor.moveToFirst()) {
                View noNotif = getView().findViewById(R.id.noNotificationsText);
                if (noNotif != null) noNotif.setVisibility(View.GONE);
                int count = 0;
                do {
                    View item = getLayoutInflater().inflate(R.layout.item_dashboard_notification, notificationList, false);
                    ((TextView)item.findViewById(R.id.notifTitle)).setText(cursor.getString(1));
                    ((TextView)item.findViewById(R.id.notifTime)).setText("Update");
                    notificationList.addView(item);
                    count++;
                } while (cursor.moveToNext() && count < 2);
            }
        } finally {
            if (cursor != null) cursor.close();
        }
    }
}
