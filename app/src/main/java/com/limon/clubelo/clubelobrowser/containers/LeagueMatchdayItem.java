package com.limon.clubelo.clubelobrowser.containers;

import com.limon.clubelo.clubelobrowser.data.League;

import java.util.Date;

public class LeagueMatchdayItem {
    private League league;
    private Date date;

    public LeagueMatchdayItem(League league, Date date) {
        this.league = league;
        this.date = date;
    }

    public League getLeague() {
        return league;
    }

    public Date getDate() {
        return date;
    }
}
