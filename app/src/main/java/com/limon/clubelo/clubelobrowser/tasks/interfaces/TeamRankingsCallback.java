package com.limon.clubelo.clubelobrowser.tasks.interfaces;

import com.limon.clubelo.clubelobrowser.containers.TeamRankingItem;

import java.util.List;

public interface TeamRankingsCallback {
    void onTeamRankingsReceived(List<TeamRankingItem> teamRankings);
}
