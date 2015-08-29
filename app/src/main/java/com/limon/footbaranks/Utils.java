package com.limon.footbaranks;

import com.limon.footbaranks.containers.TeamRatingItem;
import com.limon.footbaranks.data.Country;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Utils {
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

    public static int daysBetween(Calendar day1, Calendar day2){
        Calendar dayOne = (Calendar) day1.clone(), dayTwo = (Calendar) day2.clone();

        if (dayOne.get(Calendar.YEAR) == dayTwo.get(Calendar.YEAR)) {
            return dayOne.get(Calendar.DAY_OF_YEAR) - dayTwo.get(Calendar.DAY_OF_YEAR);
        } else {
            if (dayTwo.get(Calendar.YEAR) > dayOne.get(Calendar.YEAR)) {
                Calendar temp = dayOne;
                dayOne = dayTwo;
                dayTwo = temp;
            }

            int extraDays = 0;
            while (dayOne.get(Calendar.YEAR) > dayTwo.get(Calendar.YEAR)) {
                dayOne.add(Calendar.YEAR, -1);
                extraDays += dayOne.getActualMaximum(Calendar.DAY_OF_YEAR);
            }

            return day1.compareTo(day2) * (extraDays - dayTwo.get(Calendar.DAY_OF_YEAR) + dayOne.get(Calendar.DAY_OF_YEAR));
        }
    }
}
