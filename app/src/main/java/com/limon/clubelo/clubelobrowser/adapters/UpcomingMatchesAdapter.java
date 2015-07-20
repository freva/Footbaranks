package com.limon.clubelo.clubelobrowser.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.limon.clubelo.clubelobrowser.R;
import com.limon.clubelo.clubelobrowser.containers.MatchItem;
import java.util.List;

public class UpcomingMatchesAdapter extends ArrayAdapter<MatchItem> {
    public UpcomingMatchesAdapter(Context context, List<MatchItem> matches) {
        super(context, 0, matches);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MatchItem match = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.matches_match_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.countryFlag1 = (ImageView) convertView.findViewById(R.id.matches_match_item_countryFlag1);
            viewHolder.teamName1 = (TextView) convertView.findViewById(R.id.matches_match_item_teamName1);
            viewHolder.countryFlag2 = (ImageView) convertView.findViewById(R.id.matches_match_item_countryFlag2);
            viewHolder.teamName2 = (TextView) convertView.findViewById(R.id.matches_match_item_teamName2);

            viewHolder.percentHome = (TextView) convertView.findViewById(R.id.matches_match_item_percent_home);
            viewHolder.percentDraw = (TextView) convertView.findViewById(R.id.matches_match_item_percent_draw);
            viewHolder.percentAway = (TextView) convertView.findViewById(R.id.matches_match_item_percent_away);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.countryFlag1.setImageResource(match.getCountry().getFlagID());
        viewHolder.teamName1.setText(match.getTeamHome());
        viewHolder.countryFlag2.setImageResource(match.getCountry().getFlagID());
        viewHolder.teamName2.setText(match.getTeamAway());

        viewHolder.percentHome.setText(String.format("%.1f", match.getHomeWinProbability()*100) + "%");
        viewHolder.percentDraw.setText(String.format("%.1f", match.getDrawProbability()*100) + "%");
        viewHolder.percentAway.setText(String.format("%.1f", match.getAwayWinProbability()*100) + "%");

        return convertView;
    }


    private static class ViewHolder {
        private ImageView countryFlag1, countryFlag2;
        private TextView teamName1, teamName2, percentHome, percentDraw, percentAway;
    }
}