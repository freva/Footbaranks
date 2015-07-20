package com.limon.clubelo.clubelobrowser.containers;

import com.limon.clubelo.clubelobrowser.data.Country;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MatchItem {
    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    private Country country;
    private Calendar dateFrom;
    private String teamHome, teamAway;
    private float[] goalDifferenceTable = new float[13];
    private float[][] outcomeProbability = new float[7][7];

    public MatchItem(String... data) {
        this.dateFrom = Calendar.getInstance();
        try {
            dateFrom.setTime(df.parse(data[0]));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        this.country = Country.getCountry(data[1]);
        this.teamHome = data[2];
        this.teamAway = data[3];

        for(int i=0; i<goalDifferenceTable.length; i++)
            goalDifferenceTable[i] = Float.parseFloat(data[4+i]);

        for(int i=0, counter=0; i<outcomeProbability.length; i++) {
            for(int j=0; j<=i; j++) {
                outcomeProbability[j][i-j] = Float.parseFloat(data[17 + counter++]);
            }
        }
    }

    public Country getCountry() {
        return country;
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
}
