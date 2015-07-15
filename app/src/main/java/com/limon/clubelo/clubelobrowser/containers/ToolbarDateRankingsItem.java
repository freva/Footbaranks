package com.limon.clubelo.clubelobrowser.containers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ToolbarDateRankingsItem {
    private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    private String title;
    private Date date;

    public ToolbarDateRankingsItem(String title, Date date) {
        this.title = title;
        this.date = date;
    }
    public String getTitle() {
        return title;
    }

    public String getValue() {
        return df.format(date);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
