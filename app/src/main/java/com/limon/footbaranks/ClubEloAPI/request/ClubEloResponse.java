package com.limon.footbaranks.ClubEloAPI.request;

public class ClubEloResponse {
    private String resourceID;
    private String response;
    private Object parsedResponse;
    private boolean fromDisk;

    public ClubEloResponse(String resourceID) {
        this.resourceID = resourceID;
    }

    public String getResourceID() {
        return resourceID;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public void setParsedResponse(Object parsedResponse) {
        this.parsedResponse = parsedResponse;
    }

    public Object getParsedResponse() {
        return parsedResponse;
    }

    public boolean isFromDisk() {
        return fromDisk;
    }

    public void setFromDisk(boolean fromDisk) {
        this.fromDisk = fromDisk;
    }
}
