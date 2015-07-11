package com.limon.clubelo.clubelobrowser;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import com.limon.clubelo.clubelobrowser.adapters.TeamRankingAdapter;
import com.limon.clubelo.clubelobrowser.adapters.ToolbarDateRankingsAdapter;
import com.limon.clubelo.clubelobrowser.containers.ToolbarDateRankingsItem;
import com.limon.clubelo.clubelobrowser.tasks.TeamRankingsTask;
import com.limon.clubelo.clubelobrowser.tasks.interfaces.TeamRankingsCallback;
import com.limon.clubelo.clubelobrowser.tasks.responce.TeamRanking;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class Rankings extends AppCompatActivity implements TeamRankingsCallback, AdapterView.OnItemSelectedListener {
    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    private List<TeamRanking> teamRankings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rankings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //toolbar.setNavigationIcon(R.drawable.);

        Spinner mNavigationSpinner = new Spinner(getSupportActionBar().getThemedContext());
        toolbar.addView(mNavigationSpinner);

        ToolbarDateRankingsAdapter adapter = new ToolbarDateRankingsAdapter(this.getApplicationContext());
        mNavigationSpinner.setAdapter(adapter);
        mNavigationSpinner.setOnItemSelectedListener(this);

        if(teamRankings == null) {
            Date today = new Date();
            new TeamRankingsTask(this, this).execute(df.format(today));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rankings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTeamRankingsReceived(List<TeamRanking> teamRankings) {
        this.teamRankings = teamRankings;
        TeamRankingAdapter adapter = new TeamRankingAdapter(this.getApplicationContext(), teamRankings);

        ListView listView = (ListView) findViewById(R.id.lvTeams);
        listView.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Date getDate = ((ToolbarDateRankingsItem) parent.getItemAtPosition(position)).getDate();
        new TeamRankingsTask(this, this).execute(df.format(getDate));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
