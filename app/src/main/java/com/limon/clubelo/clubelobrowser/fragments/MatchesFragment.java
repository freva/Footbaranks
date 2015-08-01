package com.limon.clubelo.clubelobrowser.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.limon.clubelo.clubelobrowser.ClubEloAPI.ClubEloAPIRequester;
import com.limon.clubelo.clubelobrowser.ClubEloAPI.request.ClubEloResponse;
import com.limon.clubelo.clubelobrowser.ClubEloAPI.request.OnClubEloReply;
import com.limon.clubelo.clubelobrowser.MainActivity;
import com.limon.clubelo.clubelobrowser.R;
import com.limon.clubelo.clubelobrowser.adapters.UpcomingMatchesAdapter;
import com.limon.clubelo.clubelobrowser.containers.LeagueMatchdayItem;
import com.limon.clubelo.clubelobrowser.containers.MatchItem;
import com.limon.clubelo.clubelobrowser.data.League;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


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

        ClubEloAPIRequester.getUpcomingMatches(mainActivity, this);
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
        List<MatchItem> matches = null;
        List<Object> matchItems = new ArrayList<>();
        List<LeagueMatchdayItem> leagueMatchdayItems = new ArrayList<>();
        LeagueMatchdayItem lastLeague = null;

        if(response != null) matches = (List<MatchItem>) response.getParsedResponse();
        if(matches == null) matches = new ArrayList<>();

        for(MatchItem matchItem: matches) {
            if(lastLeague == null ||
                    !lastLeague.getLeague().equals(matchItem.getLeague()) ||
                    !lastLeague.getDate().equals(matchItem.getDateFrom().getTime())) {
                lastLeague = new LeagueMatchdayItem(matchItem.getLeague(), matchItem.getDateFrom().getTime());
                leagueMatchdayItems.add(lastLeague);
            }

            lastLeague.addMatch(matchItem);
        }

        Collections.sort(leagueMatchdayItems);
        for(LeagueMatchdayItem league: leagueMatchdayItems) {
            matchItems.add(league);

            Collections.sort(league.getMatches());
            for(MatchItem matchItem: league.getMatches()) {
                matchItems.add(matchItem);
            }
        }

        upcomingMatchesAdapter = new UpcomingMatchesAdapter(mainActivity.getApplicationContext(), matchItems);
        upcomingMatchesListView.setAdapter(upcomingMatchesAdapter);
    }
}
