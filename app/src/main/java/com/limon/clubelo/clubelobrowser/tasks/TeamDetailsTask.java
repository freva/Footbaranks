package com.limon.clubelo.clubelobrowser.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.limon.clubelo.clubelobrowser.R;
import com.limon.clubelo.clubelobrowser.containers.TeamRankingItem;
import com.limon.clubelo.clubelobrowser.tasks.interfaces.TeamRankingsCallback;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TeamDetailsTask extends AsyncTask<String, Void, List<TeamRankingItem>> {
    private static final String apiURL = "http://api.clubelo.com/";
    private ProgressDialog progress;
    private TeamRankingsCallback teamRankingsCallback;

    public TeamDetailsTask(Activity activity, TeamRankingsCallback teamRankingsCallback) {
        this.teamRankingsCallback = teamRankingsCallback;
        progress = new ProgressDialog(activity);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progress.setTitle(R.string.dialog_downloading);
        progress.setMessage(progress.getContext().getString(R.string.dialog_downloading_club_details));
        progress.show();
    }

    @Override
    protected List<TeamRankingItem> doInBackground(String... teamName) {
        List<TeamRankingItem> teamDetails = new ArrayList<>();
        HttpURLConnection connection = null;

        try {
            URL url = new URL(apiURL + teamName[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.connect();
            int status = connection.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    String line = reader.readLine(); //First line is the CSV header
                    while ((line = reader.readLine()) != null) {
                        if (line.length() > 0) {
                            teamDetails.add(new TeamRankingItem(line.split(",")));
                        }
                    }
                    break;

                case 404:
                    throw new IllegalArgumentException("Invalid date"); //API doesn't actually support this yet

                default:
                    throw new UnknownError("Server could not respond");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return teamDetails;
    }

    @Override
    protected void onPostExecute(List<TeamRankingItem> teamDetailsItems) {
        progress.dismiss();
        teamRankingsCallback.onTeamRankingsReceived(teamDetailsItems);
    }
}
