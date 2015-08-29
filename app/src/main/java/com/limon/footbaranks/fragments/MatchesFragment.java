package com.limon.footbaranks.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.limon.footbaranks.ClubEloAPI.ClubEloAPIRequester;
import com.limon.footbaranks.ClubEloAPI.request.ClubEloResponse;
import com.limon.footbaranks.ClubEloAPI.request.OnClubEloReply;
import com.limon.footbaranks.MainActivity;
import com.limon.footbaranks.R;
import com.limon.footbaranks.adapters.MatchesTabAdapter;
import com.limon.footbaranks.containers.LeagueMatchdayItem;
import com.limon.footbaranks.containers.MatchItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;


public class MatchesFragment extends Fragment implements OnClubEloReply, MaterialTabListener {
    private MainActivity mainActivity;
    private MatchesTabAdapter matchesTabAdapter;
    private MaterialTabHost tabHost;
    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        View rootView = inflater.inflate(R.layout.fragment_matches, container, false);

        mainActivity = (MainActivity) getActivity();
        mainActivity.getSupportActionBar().setTitle(mainActivity.getString(R.string.drawer_item_matches));
        mainActivity.getSupportActionBar().setDisplayShowTitleEnabled(true);

        tabHost = (MaterialTabHost) rootView.findViewById(R.id.materialTabHost);
        viewPager = (ViewPager) rootView.findViewById(R.id.pager);


        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                tabHost.setSelectedNavigationItem(position);
            }
        });


        ClubEloAPIRequester.getUpcomingMatches(mainActivity, this);
        return rootView;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_date_pick).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onReplyReceived(ClubEloResponse response) {
        List<MatchItem> matches = null;
        List<LeagueMatchdayItem> leagueMatchdayItems = new ArrayList<>();
        List<List<Object>> matchItems = new ArrayList<>();

        if(response != null) matches = (List<MatchItem>) response.getParsedResponse();
        if(matches == null) matches = new ArrayList<>();

        for(MatchItem matchItem: matches) {
            LeagueMatchdayItem league = new LeagueMatchdayItem(matchItem.getLeague(), matchItem.getDateFrom().getTime());
            if(! leagueMatchdayItems.contains(league)) {
                leagueMatchdayItems.add(league);
            }

            leagueMatchdayItems.get(leagueMatchdayItems.indexOf(league)).addMatch(matchItem);
        }

        Date lastDate = new Date(0);
        Collections.sort(leagueMatchdayItems);
        for(LeagueMatchdayItem league: leagueMatchdayItems) {
            if(lastDate.getTime() < league.getDate().getTime()) {
                matchItems.add(new ArrayList<>());
                lastDate = league.getDate();
            }
            matchItems.get(matchItems.size()-1).add(league);

            Collections.sort(league.getMatches());
            for(MatchItem matchItem: league.getMatches()) {
                matchItems.get(matchItems.size()-1).add(matchItem);
            }
        }

        matchesTabAdapter = new MatchesTabAdapter(mainActivity.getSupportFragmentManager(), matchItems);
        viewPager.setAdapter(matchesTabAdapter);

        for (int i = 0; i < matchesTabAdapter.getCount(); i++) {
            tabHost.addTab(tabHost.newTab().setText(matchesTabAdapter.getPageTitle(i)).setTabListener(this));
        }
    }

    public void onTabSelected(MaterialTab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab materialTab) { }

    @Override
    public void onTabUnselected(MaterialTab materialTab) { }
}
