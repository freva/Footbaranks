package com.limon.clubelo.clubelobrowser.fragments;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import com.limon.clubelo.clubelobrowser.ClubEloAPI.ClubEloAPIRequester;
import com.limon.clubelo.clubelobrowser.ClubEloAPI.ClubEloRequestType;
import com.limon.clubelo.clubelobrowser.MainActivity;
import com.limon.clubelo.clubelobrowser.R;
import com.limon.clubelo.clubelobrowser.adapters.TeamRankingAdapter;
import com.limon.clubelo.clubelobrowser.adapters.ToolbarDateRankingsAdapter;
import com.limon.clubelo.clubelobrowser.containers.ToolbarDateRankingsItem;

import com.limon.clubelo.clubelobrowser.ClubEloAPI.ClubEloResponse;
import com.limon.clubelo.clubelobrowser.ClubEloAPI.downloader.DownloaderCallback;
import com.limon.clubelo.clubelobrowser.containers.TeamRatingItem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class RankingsFragment extends Fragment implements DownloaderCallback {
    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    private static final long minDate = -977529600000L; // 10/01/1939

    private TeamRankingAdapter teamRatingsAdapter;
    private ToolbarDateRankingsAdapter filterSpinnerDateAdapter;
    private AppCompatActivity appCompatActivity;
    private LinearLayout filterSpinners;
    private Spinner filterSpinnerDate;
    private ListView teamRatingsListView;
    private View rootView;
    private Date lastDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appCompatActivity = (MainActivity) getActivity();
        rootView = inflater.inflate(R.layout.fragment_rankings, container, false);
        filterSpinners = (LinearLayout) rootView.findViewById(R.id.fragment_rankings_filter_area);

        appCompatActivity.getSupportActionBar().setTitle(appCompatActivity.getString(R.string.drawer_item_ratings));
        appCompatActivity.getSupportActionBar().setDisplayShowTitleEnabled(true);

        filterSpinnerDateAdapter = new ToolbarDateRankingsAdapter(appCompatActivity.getApplicationContext());
        filterSpinnerDate = (Spinner) rootView.findViewById(R.id.fragment_rankings_date_spinner);
        filterSpinnerDate.setAdapter(filterSpinnerDateAdapter);
        filterSpinnerDate.setOnItemSelectedListener(new DateFilterListeners());

        Button calendarButton = (Button) rootView.findViewById(R.id.fragment_rankings_calendar_button);
        calendarButton.setOnClickListener(new DateFilterListeners());

        teamRatingsListView = (ListView) rootView.findViewById(R.id.lvTeams);
        teamRatingsListView.setOnItemClickListener(new ListTeamRatingListeners());
        teamRatingsListView.setOnScrollListener(new ListTeamRatingListeners());

        return rootView;
    }


    private void getRatings(Date date) {
        if(!date.equals(lastDate)) {
            lastDate = date;
            ClubEloAPIRequester.getAPI(appCompatActivity.getApplicationContext()).getResource(
                    new ClubEloResponse(df.format(date), ClubEloRequestType.TEAM_RATINGS, this));
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        if(teamRatingsAdapter != null) teamRatingsListView.setAdapter(teamRatingsAdapter);
    }


    @Override
    public void onResponseReceived(ClubEloResponse response) {
        teamRatingsAdapter = new TeamRankingAdapter(appCompatActivity.getApplicationContext(),(List<TeamRatingItem>) response.getResponse());
        teamRatingsListView.setAdapter(teamRatingsAdapter);
    }



    private class DateFilterListeners implements AdapterView.OnItemSelectedListener, View.OnClickListener, DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar cal = Calendar.getInstance();
            cal.set(year, monthOfYear, dayOfMonth);
            filterSpinnerDateAdapter.setCustomDate(cal.getTime());
            filterSpinnerDate.setSelection(filterSpinnerDateAdapter.getCount()-1);
        }


        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Date date = ((ToolbarDateRankingsItem) parent.getItemAtPosition(position)).getDate();
            getRatings(date);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) { }

        @Override
        public void onClick(View v) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(lastDate);
            DatePickerDialog dpd = new DatePickerDialog(appCompatActivity, new DateFilterListeners(), cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            dpd.getDatePicker().setMinDate(minDate);
            dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
            dpd.show();
        }
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
