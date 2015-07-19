package com.limon.clubelo.clubelobrowser;

import com.limon.clubelo.clubelobrowser.containers.TeamRatingItem;
import com.limon.clubelo.clubelobrowser.data.Country;

import java.util.HashMap;
import java.util.List;

public class Stats {
    public static HashMap<Country, Integer> countCountryFrequency(List<TeamRatingItem> teams) {
        HashMap<Country, Integer> frequencies = new HashMap<>();
        frequencies.put(Country.ALL, teams.size());

        for(TeamRatingItem team : teams) {
            if(! frequencies.containsKey(team.getCountry())) {
                frequencies.put(team.getCountry(), 1);
            } else {
                frequencies.put(team.getCountry(), frequencies.get(team.getCountry()) + 1);
            }
        }

        return frequencies;
    }
}
