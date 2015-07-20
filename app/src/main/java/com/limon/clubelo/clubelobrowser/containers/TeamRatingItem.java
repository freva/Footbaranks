package com.limon.clubelo.clubelobrowser.containers;


import com.limon.clubelo.clubelobrowser.data.Country;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TeamRatingItem {
    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    private int rank, level;
    private Country country;
    private String clubName;
    private double elo;
    private Calendar dateFrom;

    public TeamRatingItem(String... data) {
        this.rank = (data[0].length() > 2 ? -1 : Integer.parseInt(data[0]));
        this.clubName = data[1];
        this.country = Country.getCountry(data[2]);
        this.level = Integer.parseInt(data[3]);
        this.elo = Double.parseDouble(data[4]);
        this.dateFrom = Calendar.getInstance();
        try {
            dateFrom.setTime(df.parse(data[5]));
        } catch (ParseException e) {
            e.printStackTrace();
        }
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

    public Country getCountry() {
        return country;
    }

    public double getElo() {
        return elo;
    }

    public Calendar getDateFrom() {
        return (Calendar) dateFrom.clone();
    }
}
