package com.limon.clubelo.clubelobrowser.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.limon.clubelo.clubelobrowser.R;
import com.limon.clubelo.clubelobrowser.data.Country;

import java.util.Arrays;
import java.util.List;

public class FilterCountriesAdapter extends ArrayAdapter<Country> {
    private static final List<Country> mItems = Arrays.asList(Country.values());

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

        float density = view.getResources().getDisplayMetrics().density;;
        TextView dateValue = (TextView) view.findViewById(R.id.filterSpinnerCountryName);
        Drawable flag = ContextCompat.getDrawable(view.getContext(), mItems.get(position).getFlagID());
        flag.setBounds(0, 0, (int) (30*density), (int) (25*density));

        dateValue.setText(mItems.get(position).getCountryName());
        dateValue.setCompoundDrawables(flag, null, null, null);
        dateValue.setCompoundDrawablePadding((int) (6*density));

        return view;
    }
}
