package com.limon.footbaranks.ClubEloAPI.downloader;

import com.limon.footbaranks.ClubEloAPI.request.ClubEloRequest;

public interface DownloaderCallback {
    void onDownloadReceived(ClubEloRequest request);
}
