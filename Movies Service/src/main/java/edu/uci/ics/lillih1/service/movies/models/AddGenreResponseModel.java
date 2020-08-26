package edu.uci.ics.lillih1.service.movies.models;

public class AddGenreResponseModel {
    private int resultCode;
    private String message;

    public AddGenreResponseModel() {
    }

    public AddGenreResponseModel(int resultCode, String message) {
        this.resultCode = resultCode;
        this.message = message;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
