package com.limon.footbaranks.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.limon.footbaranks.MainActivity;
import com.limon.footbaranks.R;
import com.limon.footbaranks.adapters.UpcomingMatchesAdapter;

import java.util.List;


public class MatchDayFragment extends Fragment {
    private UpcomingMatchesAdapter upcomingMatchesAdapter;
    private ListView upcomingMatchesListView;
    private List matchItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        View rootView = inflater.inflate(R.layout.matches_tab, container, false);

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.getSupportActionBar().setTitle(mainActivity.getString(R.string.drawer_item_matches));
        mainActivity.getSupportActionBar().setDisplayShowTitleEnabled(true);

        upcomingMatchesAdapter = new UpcomingMatchesAdapter(mainActivity.getApplicationContext(), matchItems);
        upcomingMatchesListView = (ListView) rootView.findViewById(R.id.matches_upcoming_list);
        upcomingMatchesListView.setAdapter(upcomingMatchesAdapter);

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

    public void setUpcomingMatches(List matchItems) {
        this.matchItems = matchItems;
    }
}
