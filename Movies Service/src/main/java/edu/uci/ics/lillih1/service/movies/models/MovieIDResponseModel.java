package edu.uci.ics.lillih1.service.movies.models;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovieIDResponseModel {
    private int resultCode;
    private String message;
    private MovieModel movie;

    public MovieIDResponseModel() {
    }

    public MovieIDResponseModel(int resultCode, String message) {
        this.resultCode = resultCode;
        this.message = message;
    }

    public MovieIDResponseModel(int resultCode, String message, MovieModel movie) {
        this.resultCode = resultCode;
        this.message = message;
        this.movie = movie;
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

    public MovieModel getMovie() {
        return movie;
    }

    public void setMovie(MovieModel movie) {
        this.movie = movie;
    }

}
