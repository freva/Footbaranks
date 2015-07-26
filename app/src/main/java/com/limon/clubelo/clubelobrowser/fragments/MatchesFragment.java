package com.limon.clubelo.clubelobrowser.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.limon.clubelo.clubelobrowser.ClubEloAPI.request.ClubEloResponse;
import com.limon.clubelo.clubelobrowser.ClubEloAPI.request.OnClubEloReply;
import com.limon.clubelo.clubelobrowser.MainActivity;
import com.limon.clubelo.clubelobrowser.R;
import com.limon.clubelo.clubelobrowser.adapters.UpcomingMatchesAdapter;


public class MatchesFragment extends Fragment implements OnClubEloReply {
    private UpcomingMatchesAdapter upcomingMatchesAdapter;
    private ListView upcomingMatchesListView;
    private MainActivity mainActivity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        View rootView = inflater.inflate(R.layout.fragment_matches, container, false);
        mainActivity = (MainActivity) getActivity();

        mainActivity.getSupportActionBar().setTitle(mainActivity.getString(R.string.drawer_item_matches));
        mainActivity.getSupportActionBar().setDisplayShowTitleEnabled(true);

        upcomingMatchesListView = (ListView) rootView.findViewById(R.id.matches_upcoming_list);


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
    public void onReplyReceived(ClubEloResponse response) {
        //upcomingMatchesAdapter = new UpcomingMatchesAdapter(mainActivity.getApplicationContext(), matches);
        upcomingMatchesListView.setAdapter(upcomingMatchesAdapter);
    }
}
