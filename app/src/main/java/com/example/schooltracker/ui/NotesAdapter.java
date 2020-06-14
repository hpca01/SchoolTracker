package com.example.schooltracker.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schooltracker.R;
import com.example.schooltracker.models.entities.Note;
import com.example.schooltracker.utils.CombinedRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {
    private static final String TAG = "NotesAdapter";
    private List<Note> notes = new ArrayList<>();
    private final LayoutInflater inflater;
    private CombinedRecyclerView clickable;

    public NotesAdapter(Context context, CombinedRecyclerView clickable) {
        this.clickable = clickable;
        this.inflater = LayoutInflater.from(context);
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotesAdapter.NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = this.inflater.inflate(R.layout.notes_list_recycler, parent, false);
        return new NoteViewHolder(v, this.clickable);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.NoteViewHolder holder, int position) {
        if (!this.notes.isEmpty()){
            Note note = this.notes.get(position);
            holder.notesText.setText(note.getNoteText());
        }
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener{
        private final CombinedRecyclerView clickable;

        @BindView(R.id.noteText)
        public TextView notesText;

        public NoteViewHolder(@NonNull View itemView, CombinedRecyclerView clickable) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.clickable = clickable;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            this.clickable.notesClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            this.clickable.notesLongClick(getAdapterPosition());
            return true;
        }
    }
}
