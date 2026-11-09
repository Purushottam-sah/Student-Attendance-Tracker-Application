package com.example.smartstudentattendancetracker.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstudentattendancetracker.R;
import com.example.smartstudentattendancetracker.adapters.StudentAdapter;
import com.example.smartstudentattendancetracker.database.SQLiteHelper;
import com.example.smartstudentattendancetracker.models.Student;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class StudentManagementActivity extends AppCompatActivity implements StudentAdapter.OnStudentActionListener {

    private RecyclerView recyclerView;
    private TextView textNoStudents;
    private FloatingActionButton fab;
    private SQLiteHelper database;
    private ArrayList<Student> list;
    private StudentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_management);

        database = new SQLiteHelper(this);
        initViews();

        fab.setOnClickListener(v -> startActivity(new Intent(this, AddStudentActivity.class)));
        
        loadStudents();
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbarStudentMgmt);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(v -> onBackPressed());
        }

        recyclerView = findViewById(R.id.recyclerStudents);
        textNoStudents = findViewById(R.id.textNoStudents);
        fab = findViewById(R.id.fabAddStudent);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStudents();
    }

    private void loadStudents() {
        list = new ArrayList<>();
        Cursor cursor = database.getAllStudents();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(new Student(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4)
                ));
            } while (cursor.moveToNext());
            cursor.close();
        }

        if (list.isEmpty()) {
            textNoStudents.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            textNoStudents.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter = new StudentAdapter(list, this);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onEdit(Student student) {
        Intent intent = new Intent(this, AddStudentActivity.class);
        intent.putExtra("id", student.getId());
        intent.putExtra("name", student.getName());
        intent.putExtra("email", student.getEmail());
        intent.putExtra("roll", student.getRollNumber());
        intent.putExtra("class", student.getClassName());
        startActivity(intent);
    }

    @Override
    public void onDelete(int id, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Student")
                .setMessage("Are you sure you want to remove this student?")
                .setPositiveButton("Remove", (dialog, which) -> {
                    database.deleteStudent(id);
                    list.remove(position);
                    adapter.notifyItemRemoved(position);
                    Toast.makeText(this, "Student removed", Toast.LENGTH_SHORT).show();
                    if (list.isEmpty()) {
                        textNoStudents.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onManage(Student student) {
        Intent intent = new Intent(this, AdminStudentSubjectsActivity.class);
        intent.putExtra("name", student.getName());
        intent.putExtra("email", student.getEmail());
        startActivity(intent);
    }
}