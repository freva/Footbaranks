package com.limon.clubelo.clubelobrowser.tasks.interfaces;

import android.app.Activity;

import com.limon.clubelo.clubelobrowser.tasks.responce.TeamRankingsResponse;

public abstract class TeamRankingsCallback extends Activity {
    public abstract void onTeamRankingsReceived(TeamRankingsResponse trr);
}
