package com.limon.clubelo.clubelobrowser.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.limon.clubelo.clubelobrowser.ClubEloAPI.ClubEloAPIRequester;
import com.limon.clubelo.clubelobrowser.ClubEloAPI.ClubEloRequestType;
import com.limon.clubelo.clubelobrowser.ClubEloAPI.ClubEloResponse;
import com.limon.clubelo.clubelobrowser.ClubEloAPI.downloader.DownloaderCallback;
import com.limon.clubelo.clubelobrowser.MainActivity;
import com.limon.clubelo.clubelobrowser.R;
import com.limon.clubelo.clubelobrowser.adapters.UpcomingMatchesAdapter;
import com.limon.clubelo.clubelobrowser.containers.MatchItem;

import java.util.ArrayList;
import java.util.List;


public class MatchesFragment extends Fragment implements DownloaderCallback {
    private UpcomingMatchesAdapter upcomingMatchesAdapter;
    private ListView upcomingMatchesListView;
    private MainActivity appCompatActivity;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        View rootView = inflater.inflate(R.layout.fragment_matches, container, false);
        appCompatActivity = (MainActivity) getActivity();

        appCompatActivity.getSupportActionBar().setTitle(appCompatActivity.getString(R.string.drawer_item_matches));
        appCompatActivity.getSupportActionBar().setDisplayShowTitleEnabled(true);

        upcomingMatchesListView = (ListView) rootView.findViewById(R.id.matches_upcoming_list);
        ClubEloAPIRequester.getAPI(appCompatActivity).getResource(
                new ClubEloResponse("Fixtures", ClubEloRequestType.MATCHES, this));

        return rootView;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_date_pick).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onResume() {
        super.onResume();

        if(upcomingMatchesAdapter != null) upcomingMatchesListView.setAdapter(upcomingMatchesAdapter);
    }

    @Override
    public void onResponseReceived(ClubEloResponse response) {
        List<MatchItem> matches = (List<MatchItem>) response.getResponse();
        if(matches == null) matches = new ArrayList<>();

        upcomingMatchesAdapter = new UpcomingMatchesAdapter(appCompatActivity.getApplicationContext(), matches);
        upcomingMatchesListView.setAdapter(upcomingMatchesAdapter);
    }
}
