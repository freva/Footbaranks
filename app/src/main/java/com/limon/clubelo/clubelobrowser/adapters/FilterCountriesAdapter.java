package com.limon.clubelo.clubelobrowser.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.limon.clubelo.clubelobrowser.R;
import com.limon.clubelo.clubelobrowser.data.Country;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class FilterCountriesAdapter extends ArrayAdapter<Country> {
    private static List<Country> mItems = Arrays.asList(Country.values());
    private HashMap<String, Integer> numberTeams;

    public FilterCountriesAdapter(Context context) {
        super(context, 0, mItems);
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {
        return getDropDownView(position, view, parent);
    }

    @Override
    public View getDropDownView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.team_rankings_country_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.countryFlag = (ImageView) view.findViewById(R.id.filterSpinnerCountryFlag);
            viewHolder.countryName = (TextView) view.findViewById(R.id.filterSpinnerCountryName);
            viewHolder.numberOfTeams = (TextView) view.findViewById(R.id.filterSpinnerCountryAmount);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }


        viewHolder.countryFlag.setImageResource(mItems.get(position).getFlagID());
        viewHolder.countryName.setText(mItems.get(position).getCountryName());
        if(this.numberTeams != null && this.numberTeams.containsKey(mItems.get(position).getCountryCode())) {
            int numTeams = this.numberTeams.get(mItems.get(position).getCountryCode());
            viewHolder.numberOfTeams.setText(numTeams + (numTeams == 1 ? " team " : " teams"));
        }

        return view;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Country getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setNumberTeams(HashMap<String, Integer> numberTeams) {
        this.numberTeams = numberTeams;

        List<Country> nTeamList = new ArrayList<>();
        for(Country country : Country.values()) {
            if(numberTeams == null || numberTeams.containsKey(country.getCountryCode())) {
                nTeamList.add(country);
            }
        }

        mItems = nTeamList;
        notifyDataSetChanged();
    }

    public int getCountryPosition(Country country) {
        return mItems.indexOf(country);
    }

    private static class ViewHolder {
        private TextView countryName, numberOfTeams;
        private ImageView countryFlag;
    }
}
