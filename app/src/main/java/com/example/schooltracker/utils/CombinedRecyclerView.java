package com.example.schooltracker.utils;

public interface CombinedRecyclerView {
    /*
     * interface to help with picking the right methods to run for the
     * view that comprises both the notes recycler and assessments recycler
     * */

    void notesClick(int position);

    void notesLongClick(int position);

    void assessmentClick(int position);

    void assessmentLongClick(int position);
}