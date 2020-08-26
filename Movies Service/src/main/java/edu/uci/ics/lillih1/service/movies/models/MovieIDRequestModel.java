package edu.uci.ics.lillih1.service.movies.models;

public class MovieIDRequestModel {
    private String email;
    private String sessionID;

    public MovieIDRequestModel(String email, String sessionID) {
        this.email = email;
        this.sessionID = sessionID;
    }

    public MovieIDRequestModel() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }
}
