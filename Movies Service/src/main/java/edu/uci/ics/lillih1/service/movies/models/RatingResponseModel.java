package edu.uci.ics.lillih1.service.movies.models;

public class RatingResponseModel {
    private int resultCode;
    private String message;

    public RatingResponseModel() {
    }

    public RatingResponseModel(int resultCode, String message) {
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
