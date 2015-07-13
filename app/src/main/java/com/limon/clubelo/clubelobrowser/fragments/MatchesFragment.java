package com.limon.clubelo.clubelobrowser.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.limon.clubelo.clubelobrowser.MainActivity;
import com.limon.clubelo.clubelobrowser.R;


public class MatchesFragment extends Fragment {
    private AppCompatActivity appCompatActivity;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rootView = inflater.inflate(R.layout.fragment_matches, container, false);
        appCompatActivity = (MainActivity) getActivity();

        appCompatActivity.getSupportActionBar().setTitle(appCompatActivity.getString(R.string.drawer_item_matches));
        appCompatActivity.getSupportActionBar().setDisplayShowTitleEnabled(true);

        return rootView;
    }
}
