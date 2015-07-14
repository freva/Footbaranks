package com.limon.clubelo.clubelobrowser.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.limon.clubelo.clubelobrowser.containers.TeamRatingItem;
import com.limon.clubelo.clubelobrowser.R;

import java.util.List;

public class TeamRankingAdapter extends ArrayAdapter<TeamRatingItem> {
    public TeamRankingAdapter(Context context, List<TeamRatingItem> teams) {
        super(context, 0, teams);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TeamRatingItem team = getItem(position);
        ViewHolder viewHolder; // view lookup downloader.cache stored in tag

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.team_ratings_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.teamRank = (TextView) convertView.findViewById(R.id.teamRank);
            viewHolder.countryFlag = (ImageView) convertView.findViewById(R.id.countryFlag);
            viewHolder.teamName = (TextView) convertView.findViewById(R.id.teamName);
            viewHolder.teamRating = (TextView) convertView.findViewById(R.id.teamRating);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        int countryResource = convertView.getResources().getIdentifier("flag_" + team.getCountryCode().toLowerCase(), "drawable", parent.getContext().getApplicationContext().getPackageName());

        viewHolder.teamRank.setText(team.getRank() != -1 ? Integer.toString(team.getRank()) : "-");
        viewHolder.countryFlag.setImageResource(countryResource);
        viewHolder.teamName.setText(team.getClubName());
        viewHolder.teamRating.setText(String.format("%.0f", team.getElo()));

        return convertView;
    }


    private static class ViewHolder {
        private TextView teamRank, teamName, teamRating;
        private ImageView countryFlag;
    }
}