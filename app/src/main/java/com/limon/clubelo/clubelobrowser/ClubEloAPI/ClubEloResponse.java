package com.limon.clubelo.clubelobrowser.ClubEloAPI;

import com.limon.clubelo.clubelobrowser.ClubEloAPI.downloader.DownloaderCallback;

public class ClubEloResponse {
    private String resourceID;
    private ClubEloRequestType requestType;
    private Object response;
    private boolean fromDisk;
    private DownloaderCallback callback;

    public ClubEloResponse(String resourceID, ClubEloRequestType requestType, DownloaderCallback callback) {
        this.resourceID = resourceID;
        this.requestType = requestType;
        this.callback = callback;
    }

    public String getResourceID() {
        return resourceID;
    }

    public ClubEloRequestType getRequestType() {
        return requestType;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    public boolean isFromDisk() {
        return fromDisk;
    }

    public void setFromDisk(boolean fromDisk) {
        this.fromDisk = fromDisk;
    }

    public DownloaderCallback getCallback() {
        return callback;
    }
}
