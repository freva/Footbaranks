package com.limon.clubelo.clubelobrowser.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.limon.clubelo.clubelobrowser.MainActivity;
import com.limon.clubelo.clubelobrowser.R;
import com.limon.clubelo.clubelobrowser.containers.TeamRatingItem;

import com.limon.clubelo.clubelobrowser.ClubEloAPI.ClubEloAPIRequester;
import com.limon.clubelo.clubelobrowser.ClubEloAPI.ClubEloRequestType;
import com.limon.clubelo.clubelobrowser.ClubEloAPI.ClubEloResponse;
import com.limon.clubelo.clubelobrowser.ClubEloAPI.downloader.DownloaderCallback;

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

public class TeamDetailsFragment extends Fragment implements DownloaderCallback {
    private MainActivity appCompatActivity;
    private PreviewLineChartView previewChart;
    private LineChartView chart;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rootView = inflater.inflate(R.layout.fragment_team_deatils, container, false);
        appCompatActivity = (MainActivity) getActivity();

        String teamName = getArguments().getString("TEAM_NAME");
        appCompatActivity.getSupportActionBar().setTitle(teamName);
        appCompatActivity.getSupportActionBar().setDisplayShowTitleEnabled(true);

        chart = (LineChartView) rootView.findViewById(R.id.chart);
        previewChart = (PreviewLineChartView) rootView.findViewById(R.id.chart_preview);

        ClubEloAPIRequester.getAPI(appCompatActivity).getResource(
                new ClubEloResponse(teamName.replace(" ", "").toLowerCase(), ClubEloRequestType.TEAM_DETAILS, this));

        return rootView;
    }


    @Override
    public void onResponseReceived(ClubEloResponse response) {
        List<TeamRatingItem> teamRatings = ((List<TeamRatingItem>) response.getResponse());
        if(teamRatings == null) {
            getFragmentManager().popBackStack();
            return;
        }

        List<PointValue> yValues = new ArrayList<>();
        List<AxisValue> xValues = new ArrayList<>();

        String lastYear = teamRatings.get(0).getDateFromString().substring(0, 4);
        for (int i = 0; i < teamRatings.size(); ++i) {
            int daySinceStart = ((int) (teamRatings.get(i).getDateFrom().getTime()/86400000)) + 11314; //Calculates days since rating start (10/01/1939)
            yValues.add(new PointValue(daySinceStart, (int) teamRatings.get(i).getElo()));

            String newYear = teamRatings.get(i).getDateFromString().substring(0, 4);
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
