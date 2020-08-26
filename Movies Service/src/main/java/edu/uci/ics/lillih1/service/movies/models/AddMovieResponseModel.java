package edu.uci.ics.lillih1.service.movies.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddMovieResponseModel {

    private int resultCode;
    private String message;
    private String movieid;
    private List<Integer> genreid;

    public AddMovieResponseModel() {
    }

    public AddMovieResponseModel(int resultCode, String message) {
        this.resultCode = resultCode;
        this.message = message;
    }

    public AddMovieResponseModel(int resultCode, String message, String movieid, List<Integer> genreid) {
        this.resultCode = resultCode;
        this.message = message;
        this.movieid = movieid;
        this.genreid = genreid;
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

    public String getMovieid() {
        return movieid;
    }

    public void setMovieid(String movieid) {
        this.movieid = movieid;
    }

    public List<Integer> getGenreid() {
        return genreid;
    }

    public void setGenreid(List<Integer> genreid) {
        this.genreid = genreid;
    }
}
