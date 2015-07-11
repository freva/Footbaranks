package com.limon.clubelo.clubelobrowser;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.limon.clubelo.clubelobrowser.containers.TeamRankingItem;
import com.limon.clubelo.clubelobrowser.tasks.TeamRankingsTask;
import com.limon.clubelo.clubelobrowser.tasks.interfaces.TeamRankingsCallback;

import java.util.ArrayList;
import java.util.List;

public class TeamDetails extends AppCompatActivity implements TeamRankingsCallback {
    private LineChart mChart;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_deatils);

        String teamName = getIntent().getExtras().getString("TEAM_NAME");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(teamName);

        mChart = (LineChart) findViewById(R.id.chart1);

        new TeamRankingsTask(this, this).execute(teamName.replace(" ", "").toLowerCase());

        //mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawGridBackground(false);
        mChart.setDescription("");

        // enable value highlighting
        mChart.setHighlightEnabled(true);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.getAxisRight().setEnabled(false);
        mChart.getAxisLeft().setStartAtZero(false);
    }

    @Override
    public void onTeamRankingsReceived(List<TeamRankingItem> teamRankings) {
        ArrayList<String> xVals = new ArrayList<>();
        ArrayList<Entry> yVals = new ArrayList<>();

        for (int i = 0; i < teamRankings.size(); i++) {
            xVals.add(teamRankings.get(i).getDateFrom());
            yVals.add(new Entry((int) teamRankings.get(i).getElo(), i));
        }

        LineDataSet set1 = new LineDataSet(yVals, "DataSet 1");
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(1f);
        set1.setCircleSize(0f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setFillAlpha(65);
        set1.setFillColor(Color.BLACK);

        ArrayList<LineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);

        mChart.setData(data);
        mChart.invalidate();
    }
}
