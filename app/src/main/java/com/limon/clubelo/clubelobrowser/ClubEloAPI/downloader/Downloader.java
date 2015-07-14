package com.limon.clubelo.clubelobrowser.ClubEloAPI.downloader;

import android.os.AsyncTask;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import com.limon.clubelo.clubelobrowser.ClubEloAPI.ClubEloResponse;


public class Downloader extends AsyncTask<ClubEloResponse, Void, ClubEloResponse> {
    private static final String apiURL = "http://api.clubelo.com/";
    private DownloaderCallback downloaderCallback;

    public Downloader(DownloaderCallback downloaderCallback) {
        this.downloaderCallback = downloaderCallback;
    }


    @Override
    protected ClubEloResponse doInBackground(ClubEloResponse... resourceName) {
        HttpURLConnection connection = null;
        ClubEloResponse response = resourceName[0];

        try {
            URL url = new URL(apiURL + response.getResourceID());
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.connect();
            int status = connection.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    StringWriter writer = new StringWriter();
                    IOUtils.copy(connection.getInputStream(), writer, "UTF-8");
                    response.setResponse(writer.toString());
                    response.setFromDisk(false);
                    break;

                case 404:
                    throw new IllegalArgumentException("Invalid resource"); //API doesn't actually support this yet

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

        return response;
    }

    @Override
    protected void onPostExecute(ClubEloResponse response) {
        downloaderCallback.onResponseReceived(response);
    }
}