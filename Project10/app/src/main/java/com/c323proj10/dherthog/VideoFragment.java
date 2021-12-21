package com.c323proj10.dherthog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class VideoFragment extends Fragment {

    private List<Video> videos;
    private ActivityCommunicator activityCommunicator;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public VideoFragment(List<Video> videos, ActivityCommunicator activityCommunicator) {
        this.videos = videos;
        this.activityCommunicator = activityCommunicator;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.revView_song);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new VideoRecyclerViewAdapter(videos, activityCommunicator));
        return view;
    }
}