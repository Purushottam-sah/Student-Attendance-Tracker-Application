package com.example.smartstudentattendancetracker.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstudentattendancetracker.R;
import com.example.smartstudentattendancetracker.adapters.HistoryAdapter;
import com.example.smartstudentattendancetracker.database.SQLiteHelper;
import com.example.smartstudentattendancetracker.models.Attendance;
import com.example.smartstudentattendancetracker.session.SessionManager;

import java.util.ArrayList;

public class AttendanceHistoryActivity extends AppCompatActivity implements HistoryAdapter.OnHistoryDeleteListener {

    private RecyclerView recyclerView;
    private TextView noHistoryText;
    private SearchView searchView;
    private Spinner filterSpinner;
    private SQLiteHelper database;
    private SessionManager session;
    private ArrayList<Attendance> historyList;
    private HistoryAdapter adapter;
    private ArrayList<String> subjectNames;
    private ArrayList<Integer> subjectIds;
    private String studentEmail;
    private boolean isAdminMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_history);

        database = new SQLiteHelper(this);
        session = new SessionManager(this);
        
        String currentRole = database.getUserRole(session.getUserEmail());
        isAdminMode = "ADMIN".equals(currentRole);

        studentEmail = getIntent().getStringExtra("userEmail");
        if (studentEmail == null) {
            studentEmail = session.getUserEmail();
        }

        initViews();
        setupFilter();
        setupSearch();

        loadAllHistory();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.historyRecycler);
        noHistoryText = findViewById(R.id.noHistoryText);
        searchView = findViewById(R.id.searchHistory);
        filterSpinner = findViewById(R.id.filterSubject);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupFilter() {
        subjectNames = new ArrayList<>();
        subjectIds = new ArrayList<>();
        subjectNames.add("All Subjects");
        subjectIds.add(-1);

        Cursor cursor = database.getSubjects(studentEmail);
        if (cursor.moveToFirst()) {
            do {
                subjectIds.add(cursor.getInt(0));
                subjectNames.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        cursor.close();

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subjectNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(spinnerAdapter);

        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    loadAllHistory();
                } else {
                    loadHistoryBySubject(subjectIds.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupSearch() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterSearch(newText);
                return true;
            }
        });
    }

    private void loadAllHistory() {
        historyList = new ArrayList<>();
        Cursor cursor;
        if (isAdminMode && studentEmail.equals(session.getUserEmail())) {
            cursor = database.getInstitutionalAttendanceHistory();
        } else {
            cursor = database.getAllAttendanceHistory(studentEmail);
        }
        parseCursor(cursor);
        updateUI();
    }

    private void loadHistoryBySubject(int subjectId) {
        historyList = new ArrayList<>();
        Cursor cursor;
        if (isAdminMode && studentEmail.equals(session.getUserEmail())) {
            cursor = database.getInstitutionalAttendanceHistoryBySubject(subjectId);
        } else {
            cursor = database.getAttendanceHistoryBySubject(subjectId, studentEmail);
        }
        parseCursor(cursor);
        updateUI();
    }

    private void parseCursor(Cursor cursor) {
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                int subId = cursor.getInt(1);
                String date = cursor.getString(2);
                String status = cursor.getString(3);
                String subName = cursor.getString(4); // Join result: subjects.name
                historyList.add(new Attendance(id, subId, subName != null ? subName : "General", date != null ? date : "", status != null ? status : "Present"));
            } while (cursor.moveToNext());
        }
        if (cursor != null) cursor.close();
    }

    private void updateUI() {
        if (historyList.isEmpty()) {
            noHistoryText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            noHistoryText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter = new HistoryAdapter(this, historyList, this);
            recyclerView.setAdapter(adapter);
        }
    }

    private void filterSearch(String text) {
        ArrayList<Attendance> filteredList = new ArrayList<>();
        for (Attendance item : historyList) {
            if (item.getDate().toLowerCase().contains(text.toLowerCase()) || 
                item.getSubjectName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        if (adapter != null) {
            adapter.filterList(filteredList);
        }
    }

    @Override
    public void onDelete(Attendance attendance, int position) {
        String role = database.getUserRole(session.getUserEmail());
        if (!role.equals("ADMIN")) {
            Toast.makeText(this, "Only Administrator can delete records", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Delete Record")
                .setMessage("Are you sure you want to delete this attendance record?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    database.deleteAttendance(attendance.getId(), attendance.getSubjectId(), attendance.getStatus());
                    historyList.remove(position);
                    adapter.notifyItemRemoved(position);
                    Toast.makeText(this, "Record deleted", Toast.LENGTH_SHORT).show();
                    if (historyList.isEmpty()) {
                        noHistoryText.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}