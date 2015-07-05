package com.limon.clubelo.clubelobrowser.tasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.limon.clubelo.clubelobrowser.tasks.interfaces.TeamRankingsCallback;
import com.limon.clubelo.clubelobrowser.tasks.responce.TeamRankingsResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TeamRankingsTask extends AsyncTask<String, Void, TeamRankingsResponse> {
    private static final String apiURL = "http://api.clubelo.com/";
    private ProgressDialog progress;
    private TeamRankingsCallback trc;

    public TeamRankingsTask(TeamRankingsCallback trc) {
        this.trc = trc;
        progress = new ProgressDialog(trc);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.show();
    }

    @Override
    protected TeamRankingsResponse doInBackground(String... date) {
        TeamRankingsResponse teamRankings = new TeamRankingsResponse();
        HttpURLConnection connection = null;

        try {
            URL url = new URL(apiURL + date[0]);
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
                            teamRankings.addTeamToRankings(line.split(","));
                        }
                    }
                    break;

                case 404:
                    teamRankings.setStatusMessage("Invalid date"); //API doesn't actually support this yet
                    break;

                default:
                    teamRankings.setStatusMessage("Server could not respond");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception e) {
                    teamRankings.setStatusMessage("Something wrong with the Internet connection");
                    e.printStackTrace();
                }
            }
        }

        return teamRankings;
    }

    @Override
    protected void onPostExecute(TeamRankingsResponse trr) {
        progress.dismiss();
        trc.onTeamRankingsReceived(trr);
    }
}
