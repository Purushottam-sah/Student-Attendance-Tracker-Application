package com.example.smartstudentattendancetracker.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstudentattendancetracker.R;
import com.example.smartstudentattendancetracker.adapters.NotesAdapter;
import com.example.smartstudentattendancetracker.database.SQLiteHelper;
import com.example.smartstudentattendancetracker.models.Note;
import com.example.smartstudentattendancetracker.session.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class NotesFragment extends Fragment implements NotesAdapter.OnNoteActionListener {

    private RecyclerView recyclerView;
    private FloatingActionButton addNote;
    private SearchView searchView;
    private SQLiteHelper database;
    private SessionManager session;
    private ArrayList<Note> noteList;
    private NotesAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_notes, container, false);

        database = new SQLiteHelper(requireContext());
        session = new SessionManager(requireContext());
        
        recyclerView = view.findViewById(R.id.notesRecycler);
        addNote = view.findViewById(R.id.addNote);
        searchView = view.findViewById(R.id.searchNotes);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        addNote.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddNoteActivity.class);
            startActivity(intent);
        });

        setupSearch();
        
        return view;
    }

    private void setupSearch() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
    }

    private void filter(String text) {
        ArrayList<Note> filteredList = new ArrayList<>();
        for (Note item : noteList) {
            if (item.getTitle().toLowerCase().contains(text.toLowerCase()) || 
                item.getDescription().toLowerCase().contains(text.toLowerCase()) ||
                item.getSubjectName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        if (adapter != null) {
            adapter.filterList(filteredList);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadNotes();
    }

    private void loadNotes() {
        noteList = new ArrayList<>();
        Cursor cursor = database.getNotes(session.getUserEmail());

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String description = cursor.getString(2);
                String subject = cursor.getString(4);
                noteList.add(new Note(id, title, description, subject));
            } while (cursor.moveToNext());
        }
        cursor.close();

        adapter = new NotesAdapter(requireContext(), noteList, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onEdit(Note note) {
        Intent intent = new Intent(getActivity(), AddNoteActivity.class);
        intent.putExtra("id", note.getId());
        intent.putExtra("title", note.getTitle());
        intent.putExtra("description", note.getDescription());
        intent.putExtra("subjectName", note.getSubjectName());
        startActivity(intent);
    }

    @Override
    public void onDelete(int id, int position) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Note")
                .setMessage("Are you sure you want to delete this note?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    database.deleteNote(id);
                    noteList.remove(position);
                    adapter.notifyItemRemoved(position);
                    Toast.makeText(requireContext(), "Note deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}