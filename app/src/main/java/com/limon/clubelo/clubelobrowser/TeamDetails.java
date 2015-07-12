package com.limon.clubelo.clubelobrowser;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.limon.clubelo.clubelobrowser.containers.TeamRankingItem;
import com.limon.clubelo.clubelobrowser.tasks.TeamDetailsTask;
import com.limon.clubelo.clubelobrowser.tasks.interfaces.TeamRankingsCallback;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ViewportChangeListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PreviewLineChartView;

public class TeamDetails extends AppCompatActivity implements TeamRankingsCallback {
    private LineChartView chart;
    private PreviewLineChartView previewChart;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_deatils);

        String teamName = getIntent().getExtras().getString("TEAM_NAME");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(teamName);

        chart = (LineChartView) findViewById(R.id.chart);
        previewChart = (PreviewLineChartView) findViewById(R.id.chart_preview);

        new TeamDetailsTask(this, this).execute(teamName.replace(" ", "").toLowerCase());
    }

    @Override
    public void onTeamRankingsReceived(List<TeamRankingItem> teamRankings) {
        List<PointValue> yValues = new ArrayList<>();
        List<AxisValue> xValues = new ArrayList<>();

        String lastYear = teamRankings.get(0).getDateFromString().substring(0, 4);
        for (int i = 0; i < teamRankings.size(); ++i) {
            int daySinceStart = ((int) (teamRankings.get(i).getDateFrom().getTime()/86400000)) + 11314; //Calculates days since rating start (10/01/1939)
            yValues.add(new PointValue(daySinceStart, (int) teamRankings.get(i).getElo()));

            String newYear = teamRankings.get(i).getDateFromString().substring(0, 4);
            if(!lastYear.equals(newYear)) {
                AxisValue axisValue = new AxisValue(daySinceStart);
                axisValue.setLabel(newYear);
                xValues.add(axisValue);
                lastYear = newYear;
            }
        }

        Line line = new Line(yValues);
        line.setColor(ChartUtils.COLOR_GREEN);
        line.setHasPoints(false);

        List<Line> lines = new ArrayList<>();
        lines.add(line);

        LineChartData data = new LineChartData(lines);
        data.setAxisXBottom(new Axis(xValues).setHasLines(true));
        data.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(4));

        // prepare preview data, is better to use separate deep copy for preview chart.
        // Set color to grey to make preview area more visible.
        LineChartData previewData = new LineChartData(data);
        previewData.getLines().get(0).setColor(ChartUtils.DEFAULT_DARKEN_COLOR);

        chart.setLineChartData(data);
        // Disable zoom/scroll for previewed chart, visible chart ranges depends on preview chart viewport so
        // zoom/scroll is unnecessary.
        chart.setZoomEnabled(false);
        chart.setScrollEnabled(false);

        previewChart.setLineChartData(previewData);
        previewChart.setViewportChangeListener(new ViewportListener());

        previewX();
    }

    private void previewX() {
        List<PointValue> values = chart.getLineChartData().getLines().get(0).getValues();
        float viewportRight = values.get(values.size() - 1).getX();

        Viewport tempViewport = new Viewport(viewportRight-5*365, 0, viewportRight, 0);
        previewChart.setCurrentViewport(tempViewport);
        previewChart.setZoomType(ZoomType.HORIZONTAL);
    }

    /**
     * Viewport listener for preview chart(lower one). in {@link #onViewportChanged(Viewport)} method change
     * viewport of upper chart.
     */
    private class ViewportListener implements ViewportChangeListener {

        @Override
        public void onViewportChanged(Viewport newViewport) {
            List<PointValue> values = chart.getLineChartData().getLines().get(0).getValues();

            float minVal = Float.MAX_VALUE, maxVal = Float.MIN_VALUE;
            for(int i=0; i < values.size() && values.get(i).getX() < newViewport.right; i+=2) {
                if(values.get(i).getX() < newViewport.left) continue;

                float val1 = values.get(i).getY();
                float val2 = values.get(i+1).getY();

                if(val1 > val2) {
                    if(val1 > maxVal) maxVal = val1;
                    if(val2 < minVal) minVal = val2;
                } else {
                    if(val2 > maxVal) maxVal = val2;
                    if(val1 < minVal) minVal = val1;
                }
            }

            newViewport.set(newViewport.left, maxVal*1.01f, newViewport.right, minVal*0.99f);
            chart.setCurrentViewport(newViewport);
        }
    }
}
