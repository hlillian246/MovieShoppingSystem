package edu.uci.ics.lillih1.service.movies.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovieSearchResponseModel {

    private int resultCode;
    private String message;
    private List<MovieModel> movies;

    public MovieSearchResponseModel() {
    }

    public MovieSearchResponseModel(int resultCode, String message) {
        this.resultCode = resultCode;
        this.message = message;
    }

    public MovieSearchResponseModel(int resultCode, String message, List<MovieModel> movies) {
        this.resultCode = resultCode;
        this.message = message;
        this.movies = movies;
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

    public List<MovieModel> getMovies() {
        return movies;
    }

    public void setMovies(List<MovieModel> movies) {
        this.movies = movies;
    }


}
