package com.limon.clubelo.clubelobrowser.tasks.interfaces;

import android.app.Activity;

import com.limon.clubelo.clubelobrowser.tasks.responce.TeamRanking;

import java.util.List;

public abstract class TeamRankingsCallback extends Activity {
    public abstract void onTeamRankingsReceived(List<TeamRanking> teamRankings);
}
