package com.example.schooltracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
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

import com.example.schooltracker.models.entities.Term;
import com.example.schooltracker.models.entities.TermWithCourses;
import com.example.schooltracker.models.entities.viewmodel.TermViewModel;
import com.example.schooltracker.ui.TermsAdapter;
import com.example.schooltracker.utils.Clickable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TermsView extends AppCompatActivity implements Clickable {

    @BindView(R.id.terms_list)
    public RecyclerView termsList;

    @BindView(R.id.toolbarLayout) public Toolbar toolbar;

    List<TermWithCourses> terms;

    TermViewModel tvm;

    TermsAdapter termsAdapter;

    final String TAG = "Terms View";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_view);
        ButterKnife.bind(this);
        terms = new ArrayList<>();
        tvm = new TermViewModel(getApplication());
        setUpAdapter();
        setupToolBar("Terms View");
    }

    private void setupToolBar(String title){
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void setUpAdapter(){
        termsAdapter = new TermsAdapter(getApplicationContext(), this);
        termsList.setAdapter(termsAdapter);
        termsList.setLayoutManager(new LinearLayoutManager(this));

        tvm.getTermWithCourse().observe(this, new Observer<List<TermWithCourses>>() {
            @Override
            public void onChanged(List<TermWithCourses> termWithCourses) {
                terms.addAll(termWithCourses);
                termsAdapter.setTerms(terms);
            }
        });
        ItemTouchHelper itemTouchHelper = addSwipeActions();
        itemTouchHelper.attachToRecyclerView(termsList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.terms_menu, menu);
        return true;
    }

    void showTermAddForm(){
        Intent newTerm = new Intent(this, NewTerm.class);
        startActivity(newTerm);
        finish();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.add_term:
                showTermAddForm();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void clicked(int position) {
        if(!terms.isEmpty()){
            TermWithCourses selectedTerm = terms.get(position);
            Intent termDetail = new Intent(this, TermDetails.class);
            termDetail.putExtra("term_id", selectedTerm.term.getId());
            startActivity(termDetail);
            finish();
        }
    }

    @Override
    public void longClick(int position) {
        if(!terms.isEmpty()){
            TermWithCourses selectedTerm = terms.get(position);
            Intent termEdit = new Intent(this, EditTerm.class);
            termEdit.putExtra("term_id", selectedTerm.term.getId());
            startActivity(termEdit);
        }
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
                TermWithCourses t = terms.get(pos);
                DialogInterface.OnClickListener dialogClick = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //yes delete
                                terms.remove(t);
                                termsAdapter.notifyItemRemoved(pos);
                                tvm.deleteTerm(t.term);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                //no delete
                                termsAdapter.notifyDataSetChanged();
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(TermsView.this);
                builder.setMessage("Are you sure you want to delete "+t.term.getTitle()+" and all associated courses?").setPositiveButton("Yes", dialogClick).setNegativeButton("No", dialogClick).show();
            }
        };
        return new ItemTouchHelper(itemTouchHelper);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
