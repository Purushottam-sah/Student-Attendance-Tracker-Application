package com.example.smartstudentattendancetracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstudentattendancetracker.R;
import com.example.smartstudentattendancetracker.database.SQLiteHelper;
import com.example.smartstudentattendancetracker.models.Note;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Note> list;
    private OnNoteActionListener listener;

    public interface OnNoteActionListener {
        void onEdit(Note note);
        void onDelete(int id, int position);
    }

    public NotesAdapter(Context context, ArrayList<Note> list, OnNoteActionListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    public void filterList(ArrayList<Note> filteredList) {
        this.list = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note note = list.get(position);

        holder.title.setText(note.getTitle());
        holder.subject.setText(note.getSubjectName());
        holder.description.setText(note.getDescription());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(note);
        });

        holder.delete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDelete(note.getId(), holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, subject, description;
        Button delete;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.noteTitleText);
            subject = itemView.findViewById(R.id.noteSubjectNameText);
            description = itemView.findViewById(R.id.noteDescriptionText);
            delete = itemView.findViewById(R.id.deleteNote);
        }
    }
}