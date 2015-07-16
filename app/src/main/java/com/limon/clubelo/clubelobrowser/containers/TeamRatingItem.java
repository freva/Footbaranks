package com.limon.clubelo.clubelobrowser.containers;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TeamRatingItem {
    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    private int rank, level;
    private String clubName, countryCode, dateFrom;
    private double elo;

    public TeamRatingItem(String... data) {
        this.rank = (data[0].length() > 2 ? -1 : Integer.parseInt(data[0]));
        this.clubName = data[1];
        this.countryCode = data[2];
        this.level = Integer.parseInt(data[3]);
        this.elo = Double.parseDouble(data[4]);
        this.dateFrom = data[5];
    }

    public int getRank() {
        return rank;
    }

    public int getLevel() {
        return level;
    }

    public String getClubName() {
        return clubName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public double getElo() {
        return elo;
    }

    public Date getDateFrom() {
        try {
            return df.parse(dateFrom);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getDateFromString() {
        return dateFrom;
    }
}
