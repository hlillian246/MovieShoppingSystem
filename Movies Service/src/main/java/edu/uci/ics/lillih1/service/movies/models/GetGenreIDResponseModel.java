package edu.uci.ics.lillih1.service.movies.models;

import java.util.ArrayList;
import java.util.List;

public class GetGenreIDResponseModel {
    private int resultCode;
    private String message;
    private List<GenreModel> genres = new ArrayList<GenreModel>();

    public GetGenreIDResponseModel() {
    }

    public GetGenreIDResponseModel(int resultCode, String message) {
        this.resultCode = resultCode;
        this.message = message;
    }

    public GetGenreIDResponseModel(int resultCode, String message, List<GenreModel> genres) {
        this.resultCode = resultCode;
        this.message = message;
        this.genres = genres;
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

    public List<GenreModel> getGenres() {
        return genres;
    }

    public void setGenres(List<GenreModel> genres) {
        this.genres = genres;
    }
}
