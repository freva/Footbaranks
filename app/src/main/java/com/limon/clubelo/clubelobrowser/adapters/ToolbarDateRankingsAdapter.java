package com.limon.clubelo.clubelobrowser.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.limon.clubelo.clubelobrowser.R;
import com.limon.clubelo.clubelobrowser.containers.ToolbarDateRankingsItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ToolbarDateRankingsAdapter extends ArrayAdapter<ToolbarDateRankingsItem> {
    private static final List<ToolbarDateRankingsItem> mItems = new ArrayList<>();

    static {
        long dayInMilliseconds = 1000 * 60 * 60 * 24;
        mItems.add(new ToolbarDateRankingsItem("Today", new Date()));
        mItems.add(new ToolbarDateRankingsItem("Week ago", new Date(System.currentTimeMillis() - (7 * dayInMilliseconds))));
        mItems.add(new ToolbarDateRankingsItem("Month ago", new Date(System.currentTimeMillis() - (30 * dayInMilliseconds))));
        mItems.add(new ToolbarDateRankingsItem("Year ago", new Date(System.currentTimeMillis() - (365 * dayInMilliseconds))));
    }

    public ToolbarDateRankingsAdapter(Context context) {
        super(context, 0, mItems);
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {
        return getDropDownView(position, view, parent);
    }

    @Override
    public View getDropDownView(int position, View view, ViewGroup parent) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_date_item, parent, false);

        TextView dateTitle = (TextView) view.findViewById(R.id.toolbarSpinnerDateTitle);
        TextView dateValue = (TextView) view.findViewById(R.id.toolbarSpinnerDateValue);

        dateTitle.setText(mItems.get(position).getTitle());
        dateValue.setText(mItems.get(position).getValue());

        return view;
    }


    public void setCustomDate(Date date) {
        if(mItems.get(mItems.size()-1).getTitle().equals("Custom")) {
            if(date == null) mItems.remove(mItems.size()-1);
            else mItems.get(mItems.size()-1).setDate(date);
        } else {
            if(date != null) mItems.add(new ToolbarDateRankingsItem("Custom", date));
        }

        notifyDataSetChanged();
    }
}
