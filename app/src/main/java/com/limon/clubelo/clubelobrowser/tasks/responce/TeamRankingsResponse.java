package com.limon.clubelo.clubelobrowser.tasks.responce;


import java.util.ArrayList;
import java.util.List;

public class TeamRankingsResponse {
    private List<String[]> rankings;
    private String statusMessage;

    public List<String[]> getTeamRankings() {
        return rankings;
    }

    public void addTeamToRankings(String[] team) {
        if(rankings == null) rankings = new ArrayList<String[]>();

        rankings.add(team);
    }

    public void setTeamRankings(List<String[]> rankings) {
        this.rankings = rankings;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public boolean isRequestOK() {
        return statusMessage == null;
    }
}
