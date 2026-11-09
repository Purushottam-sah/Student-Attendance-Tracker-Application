package com.example.smartstudentattendancetracker.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstudentattendancetracker.R;
import com.example.smartstudentattendancetracker.adapters.SubjectAdapter;
import com.example.smartstudentattendancetracker.database.SQLiteHelper;
import com.example.smartstudentattendancetracker.models.Subject;
import com.example.smartstudentattendancetracker.session.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class SubjectFragment extends Fragment {

    private RecyclerView recyclerView;
    private SQLiteHelper database;
    private SessionManager session;
    private ArrayList<Subject> subjectList;
    private String currentUserEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_subject, container, false);

        database = new SQLiteHelper(requireContext());
        session = new SessionManager(requireContext());
        
        currentUserEmail = session.getUserEmail();

        recyclerView = view.findViewById(R.id.subjectRecycler);
        FloatingActionButton addSubject = view.findViewById(R.id.addSubject);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Role-based UI
        String myRole = database.getUserRole(session.getUserEmail());
        if ("STUDENT".equals(myRole)) {
            if (addSubject != null) addSubject.setVisibility(View.GONE);
        }

        if (addSubject != null) {
            addSubject.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), AddSubjectActivity.class);
                intent.putExtra("userEmail", currentUserEmail);
                startActivity(intent);
            });
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadSubjects();
    }

    private void loadSubjects() {
        subjectList = new ArrayList<>();
        Cursor cursor = database.getSubjects(currentUserEmail);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                int total = cursor.getInt(2);
                int present = cursor.getInt(3);

                subjectList.add(new Subject(id, name, total, present));
            } while (cursor.moveToNext());
        }
        if (cursor != null) cursor.close();

        SubjectAdapter adapter = new SubjectAdapter(requireContext(), subjectList, database);
        recyclerView.setAdapter(adapter);
    }
}