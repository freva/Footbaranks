package com.limon.footbaranks.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.limon.footbaranks.Utils;
import com.limon.footbaranks.containers.LeagueMatchdayItem;
import com.limon.footbaranks.fragments.MatchDayFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MatchesTabAdapter extends FragmentStatePagerAdapter {
    private List<List<Object>> items;

    public MatchesTabAdapter(FragmentManager fm, List<List<Object>> items) {
        super(fm);

        this.items = items;
    }

    @Override
    public Fragment getItem(int position) {
        MatchDayFragment fragment = new MatchDayFragment();
        fragment.setUpcomingMatches(items.get(position));

        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return formatDate(((LeagueMatchdayItem) items.get(position).get(0)).getDate());
    }

    @Override
    public int getCount() {
        return items.size();
    }

    private static String formatDate(Date date) {
        Calendar toFormat = Calendar.getInstance();
        toFormat.setTime(date);

        switch (Utils.daysBetween(toFormat, Calendar.getInstance())) {
            case -1:
                return "Yesterday";

            case 0:
                return "Today";

            case 1:
                return "Tomorrow";

            default:
                return new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date);
        }
    }
}
