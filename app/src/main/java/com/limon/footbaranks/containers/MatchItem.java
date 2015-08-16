package com.limon.footbaranks.containers;

import com.limon.footbaranks.ClubEloAPI.ClubEloAPIRequester;
import com.limon.footbaranks.data.Country;
import com.limon.footbaranks.data.League;

import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;

public class MatchItem implements Comparable<MatchItem> {
    private Calendar dateFrom;
    private League league;
    private Country countryHome, countryAway;
    private String teamHome, teamAway;
    private float[] goalDifferenceTable = new float[13];
    private float[][] outcomeProbability = new float[7][7];
    private double eloHome, eloAway;

    public MatchItem(HashMap<String, TeamRatingItem> teams, String... data) {
        this.dateFrom = Calendar.getInstance();
        try {
            dateFrom.setTime(ClubEloAPIRequester.CLUB_ELO_DATE_FORMAT.parse(data[0]));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        this.teamHome = data[2];
        this.teamAway = data[3];

        TeamRatingItem clubH = teams.get(teamHome);
        TeamRatingItem clubA = teams.get(teamAway);

        if(Country.getCountry(data[1]) == null) {
            countryHome = clubH.getCountry();
            countryAway = clubA.getCountry();
            league = League.getLeague(Country.ALL.getCountryCode(), (data[1].equals("UCL")) ? 0 : 1);
        } else {
            countryHome = countryAway = Country.getCountry(data[1]);
            league = League.getLeague(countryHome.getCountryCode(), (clubA != null ? clubA : clubH).getLevel());
        }

        eloHome = clubH != null ? clubH.getElo() : 1000;
        eloAway = clubA != null ? clubA.getElo() : 1000;

        for(int i=0; i<goalDifferenceTable.length; i++)
            goalDifferenceTable[i] = Float.parseFloat(data[4+i]);

        for(int i=0, counter=0; i<outcomeProbability.length; i++) {
            for(int j=0; j<=i; j++) {
                outcomeProbability[j][i-j] = Float.parseFloat(data[17 + counter++]);
            }
        }
    }

    public Country getCountryHome() {
        return countryHome;
    }

    public Country getCountryAway() {
        return countryAway;
    }

    public League getLeague() {
        return league;
    }

    public Calendar getDateFrom() {
        return (Calendar) dateFrom.clone();
    }

    public String getTeamHome() {
        return teamHome;
    }

    public String getTeamAway() {
        return teamAway;
    }

    public float getScoreProbability(int goals1, int goals2) {
        return outcomeProbability[goals1][goals2];
    }

    public float getGoalDifferenceProbability(int gd) {
        return goalDifferenceTable[6 + gd];
    }


    public float getHomeWinProbability() {
        float probability = 0;
        for(int i=1; i<=6; i++)
            probability += getGoalDifferenceProbability(i);
        return probability;
    }

    public float getDrawProbability() {
        return getGoalDifferenceProbability(0);
    }

    public float getAwayWinProbability() {
        float probability = 0;
        for(int i=-6; i<0; i++)
            probability += getGoalDifferenceProbability(i);
        return probability;
    }

    public double getEloHome() {
        return eloHome;
    }

    public double getEloAway() {
        return eloAway;
    }

    @Override
    public int compareTo(MatchItem another) {
        double thisMatchElo = getEloHome() + getEloAway();
        double otherMatchElo = another.getEloHome() + another.getEloAway();
        return (int) (otherMatchElo - thisMatchElo);
    }
}
