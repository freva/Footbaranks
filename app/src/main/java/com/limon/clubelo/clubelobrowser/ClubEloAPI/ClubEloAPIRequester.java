package com.limon.clubelo.clubelobrowser.ClubEloAPI;

import android.content.Context;
import android.util.Log;

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
    private static File cacheFolder;
    private static LifetimeDiskCache lifetimeDiskCache;
    private static ClubEloAPIRequester clubEloAPIRequester;

    public static ClubEloAPIRequester getAPI(Context context) {
        if(clubEloAPIRequester == null) {
            clubEloAPIRequester = new ClubEloAPIRequester();
            cacheFolder = LifetimeDiskCache.getDiskCacheDir(context, "clubeloapicache");
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

        new Downloader(this).execute(request);
    }

    @Override
    public void onResponseReceived(ClubEloResponse response) {
        long cacheLifetime = Long.MAX_VALUE;
        String originalResponse = (String) response.getResponse();

        switch (response.getRequestType()) {
            case TEAM_RATINGS:
            case TEAM_DETAILS:
                List<TeamRatingItem> teamRatings = new ArrayList<>();
                String[] lines = originalResponse.split("\n");

                for(int i=1; i<lines.length; i++) {
                    if (lines[i].length() > 0) {
                        TeamRatingItem teamRatingItem = new TeamRatingItem(lines[i].split(","));
                        if(teamRatingItem.getDateFrom().getTime() < System.currentTimeMillis()) {
                            teamRatings.add(teamRatingItem);
                        } else {
                            cacheLifetime = teamRatingItem.getDateFrom().getTime();
                            break;
                        }
                    }
                }

                response.setResponse(teamRatings);
                if(response.getRequestType() == ClubEloRequestType.TEAM_RATINGS) cacheLifetime = System.currentTimeMillis() + WEEK_IN_MS;
                else cacheLifetime = Math.min(System.currentTimeMillis() + WEEK_IN_MS, cacheLifetime);
                break;
        }

        if(getLifetimeDiskCache() != null && ! response.isFromDisk()) {
            try {
                lifetimeDiskCache.putString(response.getResourceID(), originalResponse, cacheLifetime);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.d("Resource retrieved", "Resource '" + response.getResourceID() + "' was found on disk:" + response.isFromDisk());
        response.getCallback().onResponseReceived(response);
    }
}
