package com.limon.clubelo.clubelobrowser.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.limon.clubelo.clubelobrowser.R;
import com.limon.clubelo.clubelobrowser.data.Country;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class FilterCountriesAdapter extends ArrayAdapter<Country> {
    private static List<Country> mItems = Arrays.asList(Country.values());
    private Filter countryFilter;

    public FilterCountriesAdapter(Context context) {
        super(context, 0, mItems);
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {
        return getDropDownView(position, view, parent);
    }

    @Override
    public View getDropDownView(int position, View view, ViewGroup parent) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_country_item, parent, false);

        float density = view.getResources().getDisplayMetrics().density;
        TextView dateValue = (TextView) view.findViewById(R.id.filterSpinnerCountryName);
        Drawable flag = ContextCompat.getDrawable(view.getContext(), mItems.get(position).getFlagID());
        flag.setBounds(0, 0, (int) (30 * density), (int) (25 * density));

        dateValue.setText(mItems.get(position).getCountryName());
        dateValue.setCompoundDrawables(flag, null, null, null);
        dateValue.setCompoundDrawablePadding((int) (6*density));

        return view;
    }

    @Override
    public Filter getFilter() {
        if (countryFilter == null)
            countryFilter = new CountryFilter();

        return countryFilter;
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


    private class CountryFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            List<Country> nTeamList = new ArrayList<>();
            HashSet<String> wantedCountries = new HashSet<>(Arrays.asList(constraint.toString().split(" ", -1)));

            for(Country country : Country.values()) {
                if(wantedCountries.contains(country.getCountryCode())) {
                    nTeamList.add(country);
                }
            }

            results.values = nTeamList;
            results.count = nTeamList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count == 0) {
                notifyDataSetInvalidated();
            } else {
                mItems = (List<Country>) results.values;
                notifyDataSetChanged();
            }
        }
    }
}
