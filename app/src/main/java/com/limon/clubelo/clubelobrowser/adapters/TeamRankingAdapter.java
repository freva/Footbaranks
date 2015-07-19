package com.limon.clubelo.clubelobrowser.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.limon.clubelo.clubelobrowser.containers.TeamRatingItem;
import com.limon.clubelo.clubelobrowser.R;
import com.limon.clubelo.clubelobrowser.data.Country;

import java.util.ArrayList;
import java.util.List;

public class TeamRankingAdapter extends ArrayAdapter<TeamRatingItem> {
    private List<TeamRatingItem> originalTeamList;
    private List<TeamRatingItem> filteredTeamList;
    private Filter teamFilter;

    public TeamRankingAdapter(Context context, List<TeamRatingItem> teams) {
        super(context, 0, teams);

        originalTeamList = filteredTeamList = teams;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TeamRatingItem team = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.team_rankings_ratings_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.teamRank = (TextView) convertView.findViewById(R.id.teamRank);
            viewHolder.countryFlag = (ImageView) convertView.findViewById(R.id.countryFlag);
            viewHolder.teamName = (TextView) convertView.findViewById(R.id.teamName);
            viewHolder.teamRating = (TextView) convertView.findViewById(R.id.teamRating);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.teamRank.setText(team.getRank() != -1 ? Integer.toString(team.getRank()) : "-");
        viewHolder.countryFlag.setImageResource(team.getCountry().getFlagID());
        viewHolder.teamName.setText(team.getClubName());
        viewHolder.teamRating.setText(String.format("%.0f", team.getElo()));

        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (teamFilter == null)
            teamFilter = new TeamFilter();

        return teamFilter;
    }

    @Override
    public int getCount() {
        return filteredTeamList.size();
    }

    @Override
    public TeamRatingItem getItem(int position) {
        return filteredTeamList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    private class TeamFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            List<TeamRatingItem> nTeamList = new ArrayList<>();
            String[] constraints = constraint.toString().split("\\|", -1);

            if(constraints.length == 2) {
                String clubSearch = constraints[0].toLowerCase();
                Country countrySearch = Country.getCountry(constraints[1]);


                for (TeamRatingItem team : originalTeamList) {
                    if (countrySearch == Country.ALL || team.getCountry() == countrySearch) {
                        if (team.getClubName().toLowerCase().startsWith(clubSearch)) {
                            nTeamList.add(team);
                        } else {
                            final String[] words = team.getClubName().toLowerCase().split(" ");

                            // Start at index 0, in case valueText starts with space(s)
                            for (String word : words) {
                                if (word.startsWith(clubSearch)) {
                                    nTeamList.add(team);
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            results.values = nTeamList;
            results.count = nTeamList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredTeamList = (List<TeamRatingItem>) results.values;
            notifyDataSetChanged();
        }
    }


    private static class ViewHolder {
        private TextView teamRank, teamName, teamRating;
        private ImageView countryFlag;
    }
}