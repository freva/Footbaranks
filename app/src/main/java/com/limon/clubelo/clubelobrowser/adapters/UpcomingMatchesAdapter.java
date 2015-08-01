package com.limon.clubelo.clubelobrowser.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hb.views.PinnedSectionListView;
import com.limon.clubelo.clubelobrowser.R;
import com.limon.clubelo.clubelobrowser.containers.LeagueMatchdayItem;
import com.limon.clubelo.clubelobrowser.containers.MatchItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UpcomingMatchesAdapter extends ArrayAdapter<Object> implements PinnedSectionListView.PinnedSectionListAdapter {
    private static final int MATCH_ITEM_ID = 0, LEAGUE_ITEM_ID = 1;

    public UpcomingMatchesAdapter(Context context, List<Object> matches) {
        super(context, 0, matches);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Object item = getItem(position);

        switch (getItemViewType(position)) {
            case MATCH_ITEM_ID:
                return populateMatchItem((MatchItem) item, convertView, parent);
            case LEAGUE_ITEM_ID:
                return populateLeagueItem((LeagueMatchdayItem) item, convertView, parent);
            default:
                return convertView;
        }
    }

    @Override
    public boolean isItemViewTypePinned(int i) {
        return getItemViewType(i) == MATCH_ITEM_ID;
    }

    @Override
    public int getItemViewType(int position) {
        if(getItem(position) instanceof MatchItem) return MATCH_ITEM_ID;
        else if(getItem(position) instanceof LeagueMatchdayItem) return LEAGUE_ITEM_ID;
        else return -1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }


    private View populateMatchItem(MatchItem match, View convertView, ViewGroup parent) {
        MatchItemViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.matches_match_item, parent, false);

            viewHolder = new MatchItemViewHolder();
            viewHolder.countryFlag1 = (ImageView) convertView.findViewById(R.id.matches_match_item_countryFlag1);
            viewHolder.teamName1 = (TextView) convertView.findViewById(R.id.matches_match_item_teamName1);
            viewHolder.countryFlag2 = (ImageView) convertView.findViewById(R.id.matches_match_item_countryFlag2);
            viewHolder.teamName2 = (TextView) convertView.findViewById(R.id.matches_match_item_teamName2);

            viewHolder.percentHome = (TextView) convertView.findViewById(R.id.matches_match_item_percent_home);
            viewHolder.percentDraw = (TextView) convertView.findViewById(R.id.matches_match_item_percent_draw);
            viewHolder.percentAway = (TextView) convertView.findViewById(R.id.matches_match_item_percent_away);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MatchItemViewHolder) convertView.getTag();
        }

        viewHolder.countryFlag1.setImageResource(match.getCountryHome().getFlagID());
        viewHolder.teamName1.setText(match.getTeamHome());
        viewHolder.countryFlag2.setImageResource(match.getCountryAway().getFlagID());
        viewHolder.teamName2.setText(match.getTeamAway());

        viewHolder.percentHome.setText(String.format("%.1f", match.getHomeWinProbability()*100) + "%");
        viewHolder.percentDraw.setText(String.format("%.1f", match.getDrawProbability()*100) + "%");
        viewHolder.percentAway.setText(String.format("%.1f", match.getAwayWinProbability()*100) + "%");

        return convertView;
    }

    private View populateLeagueItem(LeagueMatchdayItem leagueMatchDayItem, View convertView, ViewGroup parent) {
        LeagueItemViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.matches_league_item, parent, false);

            viewHolder = new LeagueItemViewHolder();
            viewHolder.leagueLogo = (ImageView) convertView.findViewById(R.id.matches_league_item_logo);
            viewHolder.leagueTitle = (TextView) convertView.findViewById(R.id.matches_league_item_title);
            viewHolder.matchDate = (TextView) convertView.findViewById(R.id.matches_league_item_date);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (LeagueItemViewHolder) convertView.getTag();
        }

        viewHolder.leagueLogo.setImageResource(leagueMatchDayItem.getLeague().getLogoID());
        viewHolder.leagueTitle.setText(leagueMatchDayItem.getLeague().getLeagueName());
        viewHolder.matchDate.setText(formatDate(leagueMatchDayItem.getDate()));

        return convertView;
    }


    private static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        SimpleDateFormat fullSdf = new SimpleDateFormat("E, dd/MM/yyyy", Locale.ENGLISH);

        Calendar toFormat = Calendar.getInstance();
        toFormat.setTime(date);

        switch (daysBetween(toFormat, Calendar.getInstance())) {
            case -1:
                return "Yesterday, " + sdf.format(date);

            case 0:
                return "Today, " + sdf.format(date);

            case 1:
                return "Tomorrow, " + sdf.format(date);

            default:
                return fullSdf.format(date);
        }
    }

    private static int daysBetween(Calendar day1, Calendar day2){
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


    private static class MatchItemViewHolder {
        private ImageView countryFlag1, countryFlag2;
        private TextView teamName1, teamName2, percentHome, percentDraw, percentAway;
    }

    private static class LeagueItemViewHolder {
        private ImageView leagueLogo;
        private TextView leagueTitle, matchDate;
    }
}