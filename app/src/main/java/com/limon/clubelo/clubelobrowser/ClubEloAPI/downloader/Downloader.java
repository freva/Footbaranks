package com.limon.clubelo.clubelobrowser.ClubEloAPI.downloader;

import android.os.AsyncTask;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import com.limon.clubelo.clubelobrowser.ClubEloAPI.request.ClubEloRequest;
import com.limon.clubelo.clubelobrowser.ClubEloAPI.request.ClubEloResponse;


public class Downloader extends AsyncTask<ClubEloRequest, Void, ClubEloRequest> {
    private static final String apiURL = "http://api.clubelo.com/";
    private DownloaderCallback downloaderCallback;

    public Downloader(DownloaderCallback downloaderCallback) {
        this.downloaderCallback = downloaderCallback;
    }


    @Override
    protected ClubEloRequest doInBackground(ClubEloRequest... resourceName) {
        HttpURLConnection connection = null;
        ClubEloRequest request = resourceName[0];

        for(ClubEloResponse response: request.getResponses()) {
            if(response.isFromDisk()) continue;

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
                        request.increaseCompletedRequestsCounter();
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
        }

        return request;
    }

    @Override
    protected void onPostExecute(ClubEloRequest response) {
        downloaderCallback.onDownloadReceived(response);
    }
}