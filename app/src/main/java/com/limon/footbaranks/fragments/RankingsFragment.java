package com.limon.footbaranks.fragments;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import com.limon.footbaranks.ClubEloAPI.ClubEloAPIRequester;
import com.limon.footbaranks.ClubEloAPI.request.OnClubEloReply;
import com.limon.footbaranks.MainActivity;
import com.limon.footbaranks.R;
import com.limon.footbaranks.Utils;
import com.limon.footbaranks.adapters.FilterCountriesAdapter;
import com.limon.footbaranks.adapters.TeamRankingAdapter;

import com.limon.footbaranks.ClubEloAPI.request.ClubEloResponse;
import com.limon.footbaranks.containers.TeamRatingItem;
import com.limon.footbaranks.data.Country;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class RankingsFragment extends Fragment implements OnClubEloReply, DatePickerDialog.OnDateSetListener {
    private static final long minDate = -977529600000L; // 10/01/1939

    private TeamRankingAdapter teamRatingsAdapter;
    private MainActivity mainActivity;
    private LinearLayout filterSpinners;
    private ListView teamRatingsListView;
    private Spinner countryFilter;
    private EditText teamSearch;
    private Date lastDate;

    private int scrollPosition, scrollTopPosition, filterMenuVisibility;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        View rootView = inflater.inflate(R.layout.fragment_rankings, container, false);
        mainActivity = (MainActivity) getActivity();
        filterSpinners = (LinearLayout) rootView.findViewById(R.id.fragment_rankings_filter_area);

        mainActivity.getSupportActionBar().setTitle(mainActivity.getString(R.string.drawer_item_ratings));
        mainActivity.getSupportActionBar().setDisplayShowTitleEnabled(true);

        teamRatingsListView = (ListView) rootView.findViewById(R.id.fragment_rankings_team_list);
        teamRatingsListView.setOnItemClickListener(new ListTeamRatingListeners());
        teamRatingsListView.setOnScrollListener(new ListTeamRatingListeners());

        teamSearch = (EditText) rootView.findViewById(R.id.fragment_rankings_team_input);
        teamSearch.addTextChangedListener(new FilterAreaListeners());

        FilterCountriesAdapter filterCountriesAdapter = new FilterCountriesAdapter(mainActivity.getApplicationContext());
        countryFilter = (Spinner) rootView.findViewById(R.id.fragment_rankings_country_spinner);
        countryFilter.setAdapter(filterCountriesAdapter);
        countryFilter.setOnItemSelectedListener(new FilterAreaListeners());

        getRatings(new Date());
        return rootView;
    }

    private void getRatings(Date date) {
        lastDate = date;
        ClubEloAPIRequester.getTeamRatings(mainActivity, date, this);

    }

    private void updateCountrySpinner(List<TeamRatingItem> teams) {
        Country prev = (Country) countryFilter.getSelectedItem();

        FilterCountriesAdapter adapter = (FilterCountriesAdapter) countryFilter.getAdapter();
        adapter.setNumberTeams(Utils.countCountryFrequency(teams));

        if(adapter.getCountryPosition(prev) < 0) countryFilter.setSelection(0);
        else countryFilter.setSelection(adapter.getCountryPosition(prev));
    }



    @Override
    public void onStop() {
        super.onStop();

        View v = teamRatingsListView.getChildAt(0);
        filterMenuVisibility = filterSpinners.getVisibility();
        scrollPosition = teamRatingsListView.getFirstVisiblePosition();
        scrollTopPosition = (v == null) ? 0 : (v.getTop() - teamRatingsListView.getPaddingTop());
    }

    @Override
    public void onResume() {
        super.onResume();

        if(teamRatingsAdapter != null) teamRatingsListView.setAdapter(teamRatingsAdapter);
        filterSpinners.setVisibility(filterMenuVisibility);
        teamRatingsListView.setSelectionFromTop(scrollPosition, scrollTopPosition);
    }


    public void onReplyReceived(ClubEloResponse response) {
        List<TeamRatingItem> teams = null;

        if(response != null) teams = (List<TeamRatingItem>) response.getParsedResponse();
        if(teams == null) teams = new ArrayList<>();

        teamRatingsAdapter = new TeamRankingAdapter(mainActivity.getApplicationContext(), teams);
        teamRatingsListView.setAdapter(teamRatingsAdapter);

        updateCountrySpinner(teams);
        filterTeamList();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_date_pick).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_date_pick:
                Calendar cal = Calendar.getInstance();
                cal.setTime(lastDate);
                DatePickerDialog dpd = new DatePickerDialog(mainActivity, this, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                dpd.getDatePicker().setMinDate(minDate);
                dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
                dpd.show();
                return true;
        }
        return false;
    }

    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, monthOfYear, dayOfMonth);
        getRatings(cal.getTime());
    }

    private void filterTeamList() {
        if(teamRatingsAdapter == null) return;
        Country country = (Country) countryFilter.getSelectedItem();
        teamRatingsAdapter.getFilter().filter(teamSearch.getText() + "|" + country.getCountryCode());
    }


    private class FilterAreaListeners implements TextWatcher, AdapterView.OnItemSelectedListener {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            filterTeamList();
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void afterTextChanged(Editable s) { }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            filterTeamList();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) { }
    }


    private class ListTeamRatingListeners implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener {
        private int lastVerticalScrollPosition = 0;

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) { }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            int scrolledOffset = RankingsFragment.this.teamRatingsListView.getFirstVisiblePosition();
            if (Math.abs(scrolledOffset - lastVerticalScrollPosition) > 3) {
                if(scrolledOffset - lastVerticalScrollPosition > 0) filterSpinners.setVisibility(View.GONE);
                else filterSpinners.setVisibility(View.VISIBLE);
                lastVerticalScrollPosition = scrolledOffset;
            }
        }


        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TeamRatingItem selectedTeam = ((TeamRatingItem) parent.getItemAtPosition(position));

            Bundle bundle = new Bundle();
            bundle.putString("TEAM_NAME", selectedTeam.getClubName());

            Fragment teamDetails = new TeamDetailsFragment();
            teamDetails.setArguments(bundle);

            RankingsFragment.this.getFragmentManager().beginTransaction()
                    .replace(R.id.frame_container, teamDetails, teamDetails.getClass().getSimpleName())
                    .addToBackStack(null).commit();
        }
    }
}
