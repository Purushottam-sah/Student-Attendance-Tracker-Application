package com.example.smartstudentattendancetracker.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstudentattendancetracker.R;
import com.example.smartstudentattendancetracker.adapters.TimetableAdapter;
import com.example.smartstudentattendancetracker.database.SQLiteHelper;
import com.example.smartstudentattendancetracker.models.Timetable;
import com.example.smartstudentattendancetracker.session.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class TimetableActivity extends AppCompatActivity implements TimetableAdapter.OnTimetableDeleteListener {

    private TabLayout tabLayout;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private SQLiteHelper database;
    private SessionManager session;
    private ArrayList<Timetable> list;
    private TimetableAdapter adapter;
    private String className;

    private final String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        database = new SQLiteHelper(this);
        session = new SessionManager(this);
        
        // In full overhaul, we should fetch student's class from profiles
        className = getIntent().getStringExtra("className");
        if (className == null) {
            Cursor p = database.getProfile(session.getUserEmail());
            if (p.moveToFirst()) className = p.getString(9); // className column index in profiles
            p.close();
        }
        if (className == null) className = "BCA-1"; // Default

        initViews();
        setupTabs();

        String role = database.getUserRole(session.getUserEmail());
        fab.setVisibility(role.equals("ADMIN") ? View.VISIBLE : View.GONE);

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddTimetableActivity.class);
            intent.putExtra("className", className);
            startActivity(intent);
        });
        
        loadTimetable(days[0]);
    }

    private void initViews() {
        tabLayout = findViewById(R.id.tabLayoutDays);
        recyclerView = findViewById(R.id.recyclerTimetable);
        fab = findViewById(R.id.fabAddTimetable);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupTabs() {
        for (String day : days) {
            tabLayout.addTab(tabLayout.newTab().setText(day));
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                loadTimetable(days[tab.getPosition()]);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTimetable(days[tabLayout.getSelectedTabPosition()]);
    }

    private void loadTimetable(String day) {
        list = new ArrayList<>();
        Cursor cursor = database.getTimetableByDay(day, className);
        if (cursor.moveToFirst()) {
            do {
                list.add(new Timetable(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        adapter = new TimetableAdapter(list, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDelete(int id, int position) {
        String role = database.getUserRole(session.getUserEmail());
        if (!role.equals("ADMIN")) {
            Toast.makeText(this, "Only Administrator can modify timetable", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Delete Schedule")
                .setMessage("Remove this class from timetable?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    database.deleteTimetable(id);
                    list.remove(position);
                    adapter.notifyItemRemoved(position);
                    Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}