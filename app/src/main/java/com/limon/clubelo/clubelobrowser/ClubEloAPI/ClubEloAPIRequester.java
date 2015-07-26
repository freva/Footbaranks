package com.limon.clubelo.clubelobrowser.ClubEloAPI;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.limon.clubelo.clubelobrowser.ClubEloAPI.request.ClubEloRequest;
import com.limon.clubelo.clubelobrowser.ClubEloAPI.request.ClubEloResponse;
import com.limon.clubelo.clubelobrowser.ClubEloAPI.request.OnClubEloReply;
import com.limon.clubelo.clubelobrowser.MainActivity;
import com.limon.clubelo.clubelobrowser.R;
import com.limon.clubelo.clubelobrowser.containers.TeamRatingItem;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.limon.clubelo.clubelobrowser.ClubEloAPI.cache.LifetimeDiskCache;
import com.limon.clubelo.clubelobrowser.ClubEloAPI.downloader.Downloader;
import com.limon.clubelo.clubelobrowser.ClubEloAPI.downloader.DownloaderCallback;

public class ClubEloAPIRequester implements DownloaderCallback {
    public static final SimpleDateFormat CLUB_ELO_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    private static final long WEEK_IN_MS = 604800000;
    private static final long maxSize = 4*1024*1024; //4MB
    private static LifetimeDiskCache lifetimeDiskCache;
    private static ClubEloAPIRequester clubEloAPIRequester;
    private static ProgressDialog loadingDialog;
    private static Context mainActivity;
    private static File cacheFolder;


    public static void getTeamRatings(MainActivity activity, Date date, OnClubEloReply callback) {
        ClubEloRequest request = new ClubEloRequest(callback,
                ClubEloRequestType.TEAM_RATINGS,
                new ClubEloResponse(CLUB_ELO_DATE_FORMAT.format(date)));

        getResource(activity, request);
    }

    public static void getTeamDetails(MainActivity activity, String teamName, OnClubEloReply callback) {
        ClubEloRequest request = new ClubEloRequest(callback,
                ClubEloRequestType.TEAM_DETAILS,
                new ClubEloResponse(teamName));

        getResource(activity, request);
    }

    public static void getUpcomingMatches(MainActivity activity, OnClubEloReply callback) {
        ClubEloRequest request = new ClubEloRequest(callback,
                ClubEloRequestType.MATCHES,
                new ClubEloResponse(CLUB_ELO_DATE_FORMAT.format(new Date())),
                new ClubEloResponse("Fixtures"));

        getResource(activity, request);
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

    private static void getResource(MainActivity activity, ClubEloRequest request) {
        if(clubEloAPIRequester == null) {
            String dialogTitle = activity.getString(R.string.downloader_loading_dialog_title);
            String dialogSubtitle = activity.getString(R.string.downloader_loading_dialog_subtitle);

            clubEloAPIRequester = new ClubEloAPIRequester();
            cacheFolder = LifetimeDiskCache.getDiskCacheDir(activity, "clubeloapicache");
            loadingDialog = ProgressDialog.show(activity, dialogTitle, dialogSubtitle, true);
            mainActivity = activity;
        }

        loadingDialog.show();

        for(ClubEloResponse response : request.getResponses()) {
            try {
                if (getLifetimeDiskCache() != null) {
                    String data = lifetimeDiskCache.getString(response.getResourceID());
                    if (data != null) {
                        response.setResponse(data);
                        response.setFromDisk(true);
                        request.increaseCompletedRequestsCounter();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(request.isAllRequestsCompleted()) {
            clubEloAPIRequester.onDownloadReceived(request);
        } else if(isNetworkAvailable()) {
            new Downloader(clubEloAPIRequester).execute(request);
        } else {
            Toast.makeText(mainActivity, R.string.downloader_error_internet, Toast.LENGTH_LONG).show();
            loadingDialog.hide();
            request.getCallback().onReplyReceived(null);
        }
    }

    @Override
    public void onDownloadReceived(ClubEloRequest request) {
        if(request.isAllRequestsCompleted()) {
            long cacheLifetime = Long.MAX_VALUE;
            ClubEloResponse response = null;
            String[] rawResponse;

            switch(request.getRequestType()) {
                case TEAM_DETAILS:
                case TEAM_RATINGS:
                    response = request.getResponses()[0];
                    rawResponse = response.getResponse().split("\n");
                    List<TeamRatingItem> ratings = ClubEloParser.parseTeamRatings(rawResponse);
                    response.setParsedResponse(ratings);

                    if (request.getRequestType() == ClubEloRequestType.TEAM_RATINGS) {
                        cacheLifetime = System.currentTimeMillis() + WEEK_IN_MS;
                    } else {
                        if(ratings != null && ratings.size() > 0)
                            cacheLifetime = ratings.get(ratings.size()-1).getDateFrom().getTime().getTime();
                        cacheLifetime = Math.min(System.currentTimeMillis() + WEEK_IN_MS, cacheLifetime);
                    }
                    break;

                case MATCHES:
                    rawResponse = request.getResponses()[0].getResponse().split("\n");
                    HashMap<String, TeamRatingItem> teams = new HashMap<>();
                    for(TeamRatingItem team: ClubEloParser.parseTeamRatings(rawResponse)) {
                        teams.put(team.getClubName(), team);
                    }

                    response = request.getResponses()[1];
                    rawResponse = response.getResponse().split("\n");
                    response.setParsedResponse(ClubEloParser.parseMatches(rawResponse, teams));

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(new Date());
                    cal.add(Calendar.DAY_OF_YEAR, 1);
                    cacheLifetime = cal.getTime().getTime();
                    break;
            }

            for(ClubEloResponse cacheResponse: request.getResponses()) {
                if (getLifetimeDiskCache() != null && !cacheResponse.isFromDisk()) {
                    try {
                        lifetimeDiskCache.putString(cacheResponse.getResourceID(), cacheResponse.getResponse(), cacheLifetime);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Log.d("Resource retrieved", "Resource '" + cacheResponse.getResourceID() + "' was found on disk:" + cacheResponse.isFromDisk());
            }
            request.getCallback().onReplyReceived(response);

        } else Toast.makeText(mainActivity, R.string.downloader_error_server, Toast.LENGTH_LONG).show();

        loadingDialog.hide();
    }

    private static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
