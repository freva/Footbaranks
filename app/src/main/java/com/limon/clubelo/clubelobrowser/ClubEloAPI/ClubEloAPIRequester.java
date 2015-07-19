package com.limon.clubelo.clubelobrowser.ClubEloAPI;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.limon.clubelo.clubelobrowser.MainActivity;
import com.limon.clubelo.clubelobrowser.R;
import com.limon.clubelo.clubelobrowser.containers.TeamRatingItem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.limon.clubelo.clubelobrowser.ClubEloAPI.cache.LifetimeDiskCache;
import com.limon.clubelo.clubelobrowser.ClubEloAPI.downloader.Downloader;
import com.limon.clubelo.clubelobrowser.ClubEloAPI.downloader.DownloaderCallback;

public class ClubEloAPIRequester implements DownloaderCallback {
    private static final long maxSize = 4*1024*1024; //4MB
    private static final long WEEK_IN_MS = 604800000;
    private static LifetimeDiskCache lifetimeDiskCache;
    private static ClubEloAPIRequester clubEloAPIRequester;
    private static ProgressDialog loadingDialog;
    private static Context mainActivity;
    private static File cacheFolder;

    public static ClubEloAPIRequester getAPI(MainActivity activity) {
        if(clubEloAPIRequester == null) {
            String dialogTitle = activity.getString(R.string.downloader_loading_dialog_title);
            String dialogSubtitle = activity.getString(R.string.downloader_loading_dialog_subtitle);

            clubEloAPIRequester = new ClubEloAPIRequester();
            cacheFolder = LifetimeDiskCache.getDiskCacheDir(activity, "clubeloapicache");
            loadingDialog = ProgressDialog.show(activity, dialogTitle, dialogSubtitle, true);
            mainActivity = activity;
        }
        return clubEloAPIRequester;
    }

    private static LifetimeDiskCache getLifetimeDiskCache() {
        if(lifetimeDiskCache == null) {
            try {
                lifetimeDiskCache = LifetimeDiskCache.open(cacheFolder, maxSize);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return lifetimeDiskCache;
    }

    public void getResource(ClubEloResponse request) {
        loadingDialog.show();
        try {
            if(getLifetimeDiskCache() != null) {
                String data = lifetimeDiskCache.getString(request.getResourceID());
                if (data != null) {
                    request.setResponse(data);
                    request.setFromDisk(true);
                    onResponseReceived(request);
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(isNetworkAvailable()) new Downloader(this).execute(request);
        else {
            Toast.makeText(mainActivity, R.string.downloader_error_internet, Toast.LENGTH_LONG).show();
            loadingDialog.hide();
            request.getCallback().onResponseReceived(request);
        }
    }

    @Override
    public void onResponseReceived(ClubEloResponse response) {
        long cacheLifetime = Long.MAX_VALUE;
        String originalResponse = (String) response.getResponse();

        if(originalResponse != null) {
            switch (response.getRequestType()) {
                case TEAM_RATINGS:
                case TEAM_DETAILS:
                    List<TeamRatingItem> teamRatings = new ArrayList<>();
                    String[] lines = originalResponse.split("\n");

                    for (int i = 1; i < lines.length; i++) {
                        if (lines[i].length() > 0) {
                            TeamRatingItem teamRatingItem = new TeamRatingItem(lines[i].split(","));
                            if (teamRatingItem.getDateFrom().getTime().getTime() < System.currentTimeMillis()) {
                                teamRatings.add(teamRatingItem);
                            } else {
                                cacheLifetime = teamRatingItem.getDateFrom().getTime().getTime();
                                break;
                            }
                        }
                    }

                    response.setResponse(teamRatings);
                    if (response.getRequestType() == ClubEloRequestType.TEAM_RATINGS)
                        cacheLifetime = System.currentTimeMillis() + WEEK_IN_MS;
                    else
                        cacheLifetime = Math.min(System.currentTimeMillis() + WEEK_IN_MS, cacheLifetime);
                    break;
            }

            if (getLifetimeDiskCache() != null && !response.isFromDisk()) {
                try {
                    lifetimeDiskCache.putString(response.getResourceID(), originalResponse, cacheLifetime);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Log.d("Resource retrieved", "Resource '" + response.getResourceID() + "' was found on disk:" + response.isFromDisk());
            response.getCallback().onResponseReceived(response);
        } else Toast.makeText(mainActivity, R.string.downloader_error_server, Toast.LENGTH_LONG).show();

        loadingDialog.hide();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
