package com.limon.clubelo.clubelobrowser;

import com.limon.clubelo.clubelobrowser.containers.TeamRatingItem;
import com.limon.clubelo.clubelobrowser.data.Country;

import java.util.HashMap;
import java.util.List;

public class Stats {
    public static HashMap<String, Integer> countCountryFrequency(List<TeamRatingItem> teams) {
        HashMap<String, Integer> frequencies = new HashMap<>();
        frequencies.put(Country.ALL.getCountryCode(), teams.size());

        for(TeamRatingItem team : teams) {
            if(! frequencies.containsKey(team.getCountryCode())) {
                frequencies.put(team.getCountryCode(), 1);
            } else {
                frequencies.put(team.getCountryCode(), frequencies.get(team.getCountryCode()) + 1);
            }
        }

        return frequencies;
    }
}
