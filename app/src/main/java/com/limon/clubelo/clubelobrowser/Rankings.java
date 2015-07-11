package com.limon.clubelo.clubelobrowser;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;

import com.limon.clubelo.clubelobrowser.adapters.TeamRankingAdapter;
import com.limon.clubelo.clubelobrowser.adapters.ToolbarDateRankingsAdapter;
import com.limon.clubelo.clubelobrowser.containers.ToolbarDateRankingsItem;
import com.limon.clubelo.clubelobrowser.tasks.TeamRankingsTask;
import com.limon.clubelo.clubelobrowser.tasks.interfaces.TeamRankingsCallback;
import com.limon.clubelo.clubelobrowser.containers.TeamRankingItem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import general.SpinnerTrigger;


public class Rankings extends AppCompatActivity implements TeamRankingsCallback, DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {
    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    private static final long minDate = -977529600000L; // 10/01/1939
    private Date lastDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rankings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //toolbar.setNavigationIcon(R.drawable.);

        Spinner mNavigationSpinner = new SpinnerTrigger(getSupportActionBar().getThemedContext());
        toolbar.addView(mNavigationSpinner);

        ToolbarDateRankingsAdapter adapter = new ToolbarDateRankingsAdapter(this.getApplicationContext());
        mNavigationSpinner.setAdapter(adapter);
        mNavigationSpinner.setOnItemSelectedListener(this);

        getRatings(new Date());
    }


    private void getRatings(Date date) {
        if(!date.equals(lastDate)) {
            lastDate = date;
            new TeamRankingsTask(this, this).execute(df.format(date));
        }
    }

    @Override
    public void onTeamRankingsReceived(List<TeamRankingItem> teamRankings) {
        TeamRankingAdapter adapter = new TeamRankingAdapter(this.getApplicationContext(), teamRankings);

        ListView listView = (ListView) findViewById(R.id.lvTeams);
        listView.setAdapter(adapter);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, monthOfYear, dayOfMonth);
        getRatings(cal.getTime());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Date getDate = ((ToolbarDateRankingsItem) parent.getItemAtPosition(position)).getDate();

        if(getDate == null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(lastDate);
            DatePickerDialog dpd = new DatePickerDialog(this, this, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            dpd.getDatePicker().setMinDate(minDate);
            dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
            dpd.show();
        } else {
            Date date = ((ToolbarDateRankingsItem) parent.getItemAtPosition(position)).getDate();
            getRatings(date);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }
}
