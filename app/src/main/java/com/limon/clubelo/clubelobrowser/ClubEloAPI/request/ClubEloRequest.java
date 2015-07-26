package com.limon.clubelo.clubelobrowser.ClubEloAPI.request;

public class ClubEloRequest {
    private OnClubEloReply callback;
    private ClubEloResponse[] responses;
    private ClubEloRequestType requestType;
    private int numCompletedRequests;

    public ClubEloRequest(OnClubEloReply callback, ClubEloRequestType requestType, ClubEloResponse... responses) {
        this.callback = callback;
        this.requestType = requestType;
        this.responses = responses;
    }

    public OnClubEloReply getCallback() {
        return callback;
    }

    public ClubEloResponse[] getResponses() {
        return responses;
    }

    public void increaseCompletedRequestsCounter() {
        numCompletedRequests++;
    }

    public boolean isAllRequestsCompleted() {
        return numCompletedRequests == responses.length;
    }

    public ClubEloRequestType getRequestType() {
        return requestType;
    }
}
