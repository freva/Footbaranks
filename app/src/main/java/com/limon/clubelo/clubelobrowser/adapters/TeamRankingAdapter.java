package com.limon.clubelo.clubelobrowser.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.limon.clubelo.clubelobrowser.tasks.responce.TeamRanking;
import com.limon.clubelo.clubelobrowser.R;

import java.util.List;

public class TeamRankingAdapter extends ArrayAdapter<TeamRanking> {
    public TeamRankingAdapter(Context context, List<TeamRanking> teams) {
        super(context, 0, teams);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TeamRanking team = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_team_ratings, parent, false);
        }

        int countryResource = convertView.getResources().getIdentifier("flag_" + team.getCountryCode().toLowerCase(), "drawable", parent.getContext().getApplicationContext().getPackageName());

        TextView teamRank = (TextView) convertView.findViewById(R.id.teamRank);
        ImageView countryFlag = (ImageView) convertView.findViewById(R.id.countryFlag);
        TextView teamName = (TextView) convertView.findViewById(R.id.teamName);
        TextView teamRating = (TextView) convertView.findViewById(R.id.teamRating);

        teamRank.setText(team.getRank() != -1 ? Integer.toString(team.getRank()) : "-");
        countryFlag.setImageResource(countryResource);
        teamName.setText(team.getClubName());
        teamRating.setText(String.format("%.2f", team.getElo()));

        return convertView;
    }
}