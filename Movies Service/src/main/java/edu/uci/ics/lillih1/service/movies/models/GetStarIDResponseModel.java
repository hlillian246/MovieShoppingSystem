package edu.uci.ics.lillih1.service.movies.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetStarIDResponseModel {
    private int resultCode;
    private String message;
    private List<StarModel> stars = new ArrayList<StarModel>();

    public GetStarIDResponseModel() {
    }

    public GetStarIDResponseModel(int resultCode, String message) {
        this.resultCode = resultCode;
        this.message = message;
    }

    public GetStarIDResponseModel(int resultCode, String message, List<StarModel> stars) {
        this.resultCode = resultCode;
        this.message = message;
        this.stars = stars;
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

    public List<StarModel> getStars() {
        return stars;
    }

    public void setStars(List<StarModel> stars) {
        this.stars = stars;
    }
}
