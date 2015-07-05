package com.limon.clubelo.clubelobrowser;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.limon.clubelo.clubelobrowser.tasks.TeamRankingsTask;
import com.limon.clubelo.clubelobrowser.tasks.interfaces.TeamRankingsCallback;
import com.limon.clubelo.clubelobrowser.tasks.responce.TeamRankingsResponse;


public class Rankings extends TeamRankingsCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rankings);

        new TeamRankingsTask(this).execute("2015-07-05");
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
    public void onTeamRankingsReceived(TeamRankingsResponse trr) {

    }
}
