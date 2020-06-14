package com.example.schooltracker;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.example.schooltracker.models.entities.Mentor;
import com.example.schooltracker.models.entities.viewmodel.MentorViewModel;
import com.example.schooltracker.ui.MentorAdapter;
import com.example.schooltracker.utils.Clickable;
import com.example.schooltracker.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MentorsView extends AppCompatActivity implements Clickable {

    private static final String TAG = "MentorsView";

    private MentorViewModel mvm;

    @BindView(R.id.mentor_list)
    public RecyclerView mentorsListView;

    private List<Mentor> mentorsList = new ArrayList<>();
    MentorAdapter mentorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentors_view);
        mvm = new MentorViewModel(getApplication());
        ButterKnife.bind(this);
        setupRecycler();
    }
    public void setupRecycler(){
        mentorAdapter = new MentorAdapter(getApplicationContext(), this);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        Utils.addDividers(mentorsListView, layout);
        mentorsListView.setLayoutManager(layout);
        mentorsListView.setAdapter(mentorAdapter);

        mvm.getAllMentors().observe(this, new Observer<List<Mentor>>() {
            @Override
            public void onChanged(List<Mentor> mentors) {
                mentorsList.addAll(mentors);
                mentorAdapter.setMentors(mentorsList);
            }
        });
        ItemTouchHelper itemTouchHelper = addSwipeActions();
        itemTouchHelper.attachToRecyclerView(mentorsListView);

    }

    private ItemTouchHelper addSwipeActions(){
        ItemTouchHelper.SimpleCallback itemTouchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();
                Mentor m = mentorsList.get(pos);
                DialogInterface.OnClickListener dialogClick = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //yes delete
                                    mentorsList.remove(m);
                                    mentorsListView.getAdapter().notifyItemRemoved(pos);
                                    mvm.deleteMentor(m);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                //no delete
                                mentorsListView.getAdapter().notifyDataSetChanged();
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(MentorsView.this);
                builder.setMessage("Are you sure you want to delete "+m.getName()).setPositiveButton("Yes", dialogClick).setNegativeButton("No", dialogClick).show();
            }
        };
        return new ItemTouchHelper(itemTouchHelper);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mentors_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.add_mentor:
                Intent newMentor = new Intent(this, NewMentor.class);
                startActivity(newMentor);
                break;
        }
        return true;
    }

    @Override
    public void clicked(int position) {
        Mentor mentorToEdit = mentorsList.get(position);
        Intent editMentor = new Intent(this, NewMentor.class);
        editMentor.putExtra("mentor_id", mentorToEdit.getId());
        startActivity(editMentor);
    }

    @Override
    public void longClick(int position) {
        // TODO: 4/16/2020 add code for what to do when item is long pressed, most likely delete mentor
    }
}
