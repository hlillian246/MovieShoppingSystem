package edu.uci.ics.lillih1.service.movies.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class StarIDResponseModel {
    private int resultCode;
    private String message;
    private StarModel star;

    public StarIDResponseModel() {
    }

    public StarIDResponseModel(int resultCode, String message) {
        this.resultCode = resultCode;
        this.message = message;
    }

    public StarIDResponseModel(int resultCode, String message, StarModel star) {
        this.resultCode = resultCode;
        this.message = message;
        this.star = star;
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

    public StarModel getStar() {
        return star;
    }

    public void setStars(StarModel star) {
        this.star = star;
    }
}
