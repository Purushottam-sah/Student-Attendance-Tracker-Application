package com.example.smartstudentattendancetracker.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstudentattendancetracker.R;
import com.example.smartstudentattendancetracker.adapters.NoticeAdapter;
import com.example.smartstudentattendancetracker.database.SQLiteHelper;
import com.example.smartstudentattendancetracker.models.Notice;
import com.example.smartstudentattendancetracker.session.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class NoticeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView textNoNotice;
    private FloatingActionButton fabAdd;
    private SQLiteHelper database;
    private SessionManager session;
    private ArrayList<Notice> list;
    private NoticeAdapter adapter;
    private Button btnAll, btnAcademic, btnExam, btnEvent, btnHoliday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        database = new SQLiteHelper(this);
        session = new SessionManager(this);

        recyclerView = findViewById(R.id.recyclerNotice);
        textNoNotice = findViewById(R.id.textNoNotice);
        fabAdd = findViewById(R.id.fabAddNotice);
        
        btnAll = findViewById(R.id.btnAll);
        btnAcademic = findViewById(R.id.btnAcademic);
        btnExam = findViewById(R.id.btnExam);
        btnEvent = findViewById(R.id.btnEvent);
        btnHoliday = findViewById(R.id.btnHoliday);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        setupCategoryButtons();

        // Show FAB only for Admins
        String role = database.getUserRole(session.getUserEmail());
        if (role.equals("ADMIN")) {
            fabAdd.setVisibility(View.VISIBLE);
        }

        fabAdd.setOnClickListener(v -> startActivity(new Intent(this, AddNoticeActivity.class)));

        loadNotices();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotices();
    }

    private void setupCategoryButtons() {
        View.OnClickListener listener = v -> {
            resetButtons();
            v.setBackgroundResource(R.drawable.bg_tab_selected);
            ((Button)v).setTextColor(getResources().getColor(R.color.white, getTheme()));
            
            String category = ((Button)v).getText().toString();
            filterNotices(category);
        };

        btnAll.setOnClickListener(listener);
        btnAcademic.setOnClickListener(listener);
        btnExam.setOnClickListener(listener);
        btnEvent.setOnClickListener(listener);
        btnHoliday.setOnClickListener(listener);
    }

    private void resetButtons() {
        Button[] buttons = {btnAll, btnAcademic, btnExam, btnEvent, btnHoliday};
        for (Button b : buttons) {
            b.setBackgroundResource(R.drawable.bg_tab_unselected);
            b.setTextColor(getResources().getColor(R.color.text_secondary, getTheme()));
        }
    }

    private void filterNotices(String category) {
        if (category.equals("All")) {
            loadNotices();
            return;
        }
        
        ArrayList<Notice> filteredList = new ArrayList<>();
        for (Notice n : list) {
            if (n.getTitle().toLowerCase().contains(category.toLowerCase()) || 
                n.getMessage().toLowerCase().contains(category.toLowerCase())) {
                filteredList.add(n);
            }
        }
        adapter = new NoticeAdapter(filteredList);
        recyclerView.setAdapter(adapter);
    }

    private void loadNotices() {
        list = new ArrayList<>();
        Cursor cursor = database.getNotices();
        if (cursor.moveToFirst()) {
            do {
                list.add(new Notice(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();

        if (list.isEmpty()) {
            textNoNotice.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            textNoNotice.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter = new NoticeAdapter(list);
            recyclerView.setAdapter(adapter);
        }
    }
}