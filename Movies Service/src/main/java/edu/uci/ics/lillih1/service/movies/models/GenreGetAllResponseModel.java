package edu.uci.ics.lillih1.service.movies.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenreGetAllResponseModel {
    private int resultCode;
    private String message;
    private List<GenreModel> genres;

    public GenreGetAllResponseModel() {
    }

    public GenreGetAllResponseModel(int resultCode, String message) {
        this.resultCode = resultCode;
        this.message = message;
    }

    public GenreGetAllResponseModel(int resultCode, String message, List<GenreModel> genres) {
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
