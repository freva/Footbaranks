package com.limon.clubelo.clubelobrowser.containers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ToolbarDateRankingsItem {
    private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    private String title, value;

    public ToolbarDateRankingsItem(String title, Date date) {
        this.title = title;
        this.value = df.format(date);
    }

    public ToolbarDateRankingsItem(String title) {
        this.title = title;
        this.value = "";
    }

    public String getTitle() {
        return title;
    }

    public String getValue() {
        return value;
    }
}
