package com.limon.clubelo.clubelobrowser.tasks.interfaces;

import com.limon.clubelo.clubelobrowser.tasks.responce.TeamRanking;

import java.util.List;

public interface TeamRankingsCallback {
    void onTeamRankingsReceived(List<TeamRanking> teamRankings);
}
