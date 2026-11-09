package com.example.smartstudentattendancetracker.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartstudentattendancetracker.R;
import com.example.smartstudentattendancetracker.database.SQLiteHelper;
import com.example.smartstudentattendancetracker.session.SessionManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AddNoteActivity extends AppCompatActivity {

    private EditText titleEdit, descriptionEdit;
    private AutoCompleteTextView spinnerSubject;
    private Button btnSave;
    private TextView textTitle;
    private SQLiteHelper database;
    private SessionManager session;
    private int noteId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        database = new SQLiteHelper(this);
        session = new SessionManager(this);
        initViews();
        setupSubjectSpinner();

        if (getIntent().hasExtra("id")) {
            noteId = getIntent().getIntExtra("id", -1);
            textTitle.setText("Edit Note");
            titleEdit.setText(getIntent().getStringExtra("title"));
            descriptionEdit.setText(getIntent().getStringExtra("description"));
            spinnerSubject.setText(getIntent().getStringExtra("subjectName"), false);
            btnSave.setText("Update Note");
        }

        btnSave.setOnClickListener(v -> saveNote());
    }

    private void initViews() {
        textTitle = findViewById(R.id.textAddNoteTitle);
        titleEdit = findViewById(R.id.noteTitle);
        descriptionEdit = findViewById(R.id.noteDescription);
        spinnerSubject = findViewById(R.id.spinnerNoteSubject);
        btnSave = findViewById(R.id.saveNote);
    }

    private void setupSubjectSpinner() {
        ArrayList<String> subjectNames = new ArrayList<>();
        subjectNames.add("General");
        
        Cursor cursor = database.getSubjects(session.getUserEmail());
        if (cursor.moveToFirst()) {
            do {
                subjectNames.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, subjectNames);
        spinnerSubject.setAdapter(adapter);
    }

    private void saveNote() {
        String title = titleEdit.getText().toString().trim();
        String description = descriptionEdit.getText().toString().trim();
        String subject = spinnerSubject.getText().toString();
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String email = session.getUserEmail();

        if (title.isEmpty() && description.isEmpty()) {
            Toast.makeText(this, "Please enter some text", Toast.LENGTH_SHORT).show();
            return;
        }

        if (noteId == -1) {
            if (database.addNote(title, description, date, subject, email)) {
                Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            if (updateExistingNote(noteId, title, description, date, subject, email)) {
                Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private boolean updateExistingNote(int id, String t, String d, String date, String s, String email) {
        android.content.ContentValues values = new android.content.ContentValues();
        values.put("title", t);
        values.put("description", d);
        values.put("date", date);
        values.put("subjectName", s);
        values.put("studentEmail", email);
        return database.getWritableDatabase().update("notes", values, "id=?", new String[]{String.valueOf(id)}) > 0;
    }
}