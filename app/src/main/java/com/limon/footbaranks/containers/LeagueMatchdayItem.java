package com.limon.footbaranks.containers;

import com.limon.footbaranks.data.League;

import java.util.ArrayList;
import java.util.Date;

public class LeagueMatchdayItem implements Comparable<LeagueMatchdayItem> {
    private League league;
    private Date date;
    private ArrayList<MatchItem> matches = new ArrayList<>();

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

    public ArrayList<MatchItem> getMatches() {
        return matches;
    }

    public void addMatch(MatchItem match) {
        matches.add(match);
    }

    public double getAverageElo() {
        double sum = 0;
        for(MatchItem match: matches) {
            sum += match.getEloHome() + match.getEloAway();
        }

        return sum / matches.size();
    }

    public int compareTo(LeagueMatchdayItem otherLeague) {
        if(! otherLeague.getDate().equals(getDate())) {
            return getDate().compareTo(otherLeague.getDate());
        } else {
            return (int) (getAverageElo() - otherLeague.getAverageElo());
        }
    }
}
