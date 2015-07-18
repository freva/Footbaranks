package com.limon.clubelo.clubelobrowser.fragments;

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

import com.limon.clubelo.clubelobrowser.ClubEloAPI.ClubEloAPIRequester;
import com.limon.clubelo.clubelobrowser.ClubEloAPI.ClubEloRequestType;
import com.limon.clubelo.clubelobrowser.MainActivity;
import com.limon.clubelo.clubelobrowser.R;
import com.limon.clubelo.clubelobrowser.Stats;
import com.limon.clubelo.clubelobrowser.adapters.FilterCountriesAdapter;
import com.limon.clubelo.clubelobrowser.adapters.TeamRankingAdapter;

import com.limon.clubelo.clubelobrowser.ClubEloAPI.ClubEloResponse;
import com.limon.clubelo.clubelobrowser.ClubEloAPI.downloader.DownloaderCallback;
import com.limon.clubelo.clubelobrowser.containers.TeamRatingItem;
import com.limon.clubelo.clubelobrowser.data.Country;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class RankingsFragment extends Fragment implements DownloaderCallback, DatePickerDialog.OnDateSetListener {
    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    private static final long minDate = -977529600000L; // 10/01/1939

    private TeamRankingAdapter teamRatingsAdapter;
    private MainActivity appCompatActivity;
    private LinearLayout filterSpinners;
    private ListView teamRatingsListView;
    private Spinner countryFilter;
    private EditText teamSearch;
    private Date lastDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        View rootView = inflater.inflate(R.layout.fragment_rankings, container, false);
        appCompatActivity = (MainActivity) getActivity();
        filterSpinners = (LinearLayout) rootView.findViewById(R.id.fragment_rankings_filter_area);

        appCompatActivity.getSupportActionBar().setTitle(appCompatActivity.getString(R.string.drawer_item_ratings));
        appCompatActivity.getSupportActionBar().setDisplayShowTitleEnabled(true);

        teamRatingsListView = (ListView) rootView.findViewById(R.id.lvTeams);
        teamRatingsListView.setOnItemClickListener(new ListTeamRatingListeners());
        teamRatingsListView.setOnScrollListener(new ListTeamRatingListeners());

        teamSearch = (EditText) rootView.findViewById(R.id.fragment_rankings_team_input);
        teamSearch.addTextChangedListener(new FilterAreaListeners());

        FilterCountriesAdapter filterCountriesAdapter = new FilterCountriesAdapter(appCompatActivity.getApplicationContext());
        countryFilter = (Spinner) rootView.findViewById(R.id.fragment_rankings_country_spinner);
        countryFilter.setAdapter(filterCountriesAdapter);
        countryFilter.setOnItemSelectedListener(new FilterAreaListeners());

        getRatings(new Date());

        return rootView;
    }

    private void getRatings(Date date) {
        lastDate = date;
        ClubEloAPIRequester.getAPI(appCompatActivity).getResource(
                new ClubEloResponse(df.format(date), ClubEloRequestType.TEAM_RATINGS, this));
    }

    private void updateCountrySpinner(List<TeamRatingItem> teams) {
        Country prev = (Country) countryFilter.getSelectedItem();

        FilterCountriesAdapter adapter = (FilterCountriesAdapter) countryFilter.getAdapter();
        adapter.setNumberTeams(Stats.countCountryFrequency(teams));

        if(adapter.getCountryPosition(prev) < 0) countryFilter.setSelection(0);
        else countryFilter.setSelection(adapter.getCountryPosition(prev));
    }

    @Override
    public void onResume() {
        super.onResume();

        if(teamRatingsAdapter != null) teamRatingsListView.setAdapter(teamRatingsAdapter);
    }


    @Override
    public void onResponseReceived(ClubEloResponse response) {
        List<TeamRatingItem> teams = (List<TeamRatingItem>) response.getResponse();
        if(teams == null) teams = new ArrayList<>();

        teamRatingsAdapter = new TeamRankingAdapter(appCompatActivity.getApplicationContext(), teams);
        teamRatingsListView.setAdapter(teamRatingsAdapter);

        updateCountrySpinner(teams);
        filterTeamList();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_date_pick).setVisible(true);
        super.onPrepareOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_date_pick:
                Calendar cal = Calendar.getInstance();
                cal.setTime(lastDate);
                DatePickerDialog dpd = new DatePickerDialog(appCompatActivity, this, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
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
            if (scrolledOffset != lastVerticalScrollPosition) {
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
