package com.example.schooltracker.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schooltracker.R;
import com.example.schooltracker.models.entities.Mentor;
import com.example.schooltracker.utils.Clickable;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MentorAdapter extends RecyclerView.Adapter<MentorAdapter.MentorViewHolder> {

    private List<Mentor> mentors = new ArrayList<>();
    private final LayoutInflater inflater;
    private Clickable clickable;

    public MentorAdapter(Context context, Clickable clickable) {
        inflater = LayoutInflater.from(context);
        this.clickable = clickable;
    }

    public void setMentors(List<Mentor> mentors) {
        this.mentors = mentors;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MentorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = this.inflater.inflate(R.layout.activity_mentors_view_recycler, parent, false);
        return new MentorViewHolder(v, this.clickable);
    }

    @Override
    public void onBindViewHolder(@NonNull MentorViewHolder holder, int position) {
        Mentor m = mentors.get(position);
        holder.name.setText(m.getName());
        holder.email.setText(m.getEmail());
        holder.phone.setText(m.getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return mentors.size();
    }

    public class MentorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private final Clickable clickable;

        @BindView(R.id.mentor_first_name)
        public TextView name;

        @BindView(R.id.mentor_email)
        public TextView email;

        @BindView(R.id.mentor_phone)
        public TextView phone;

        public MentorViewHolder(@NonNull View itemView, Clickable clickable) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.clickable = clickable;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View v) {
            this.clickable.clicked(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            this.clickable.longClick(getAdapterPosition());
            return true;
        }
    }
}
