package com.c323proj10.dherthog;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class SongFragment extends Fragment {

    private List<Song> songs;
    private ActivityCommunicator activityCommunicator;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SongFragment(List<Song> songs, ActivityCommunicator activityCommunicator) {
        this.songs = songs;
        this.activityCommunicator = activityCommunicator;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.revView_song);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new SongRecyclerViewAdapter(songs, activityCommunicator));
        return view;
    }
}