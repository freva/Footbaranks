package com.limon.clubelo.clubelobrowser.ClubEloAPI.downloader;

import com.limon.clubelo.clubelobrowser.ClubEloAPI.request.ClubEloRequest;

public interface DownloaderCallback {
    void onDownloadReceived(ClubEloRequest request);
}
