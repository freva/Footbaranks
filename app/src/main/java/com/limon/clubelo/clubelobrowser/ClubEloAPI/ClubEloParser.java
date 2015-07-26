package com.limon.clubelo.clubelobrowser.ClubEloAPI;

import com.limon.clubelo.clubelobrowser.containers.MatchItem;
import com.limon.clubelo.clubelobrowser.containers.TeamRatingItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClubEloParser {
    public static List<TeamRatingItem> parseTeamRatings(String[] lines) {
        List<TeamRatingItem> teamRatings = new ArrayList<>();

        for (int i = 1; i < lines.length; i++) {
            if (lines[i].length() > 0) {
                TeamRatingItem teamRatingItem = new TeamRatingItem(lines[i].split(","));
                teamRatings.add(teamRatingItem);

                if (teamRatingItem.getDateFrom().getTime().getTime() > System.currentTimeMillis()) {
                    break;
                }
            }
        }
        return teamRatings;
    }

    public static List<MatchItem> parseMatches(String[] lines, HashMap<String, TeamRatingItem> teams) {
        List<MatchItem> matches = new ArrayList<>();

        for (int i = 1; i < lines.length; i++) {
            if (lines[i].length() > 0) {
                matches.add(new MatchItem(lines[i].split(",")));
            }
        }

        return matches;
    }
}
