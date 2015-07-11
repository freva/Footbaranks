package com.limon.clubelo.clubelobrowser.containers;


public class TeamRankingItem {
    private int rank, level;
    private String clubName, countryCode, dateFrom;
    private double elo;

    public TeamRankingItem(String... data) {
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

    public String getDateFrom() {
        return dateFrom;
    }
}
