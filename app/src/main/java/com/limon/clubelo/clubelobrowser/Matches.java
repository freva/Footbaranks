package com.limon.clubelo.clubelobrowser;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class Matches extends Fragment {
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rootView = inflater.inflate(R.layout.activity_rankings, container, false);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Matches");

        return rootView;
    }
}
