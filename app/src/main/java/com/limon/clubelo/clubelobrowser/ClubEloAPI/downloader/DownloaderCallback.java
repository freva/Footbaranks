package com.limon.clubelo.clubelobrowser.ClubEloAPI.downloader;

import com.limon.clubelo.clubelobrowser.ClubEloAPI.ClubEloResponse;

public interface DownloaderCallback {
    void onResponseReceived(ClubEloResponse response);
}
