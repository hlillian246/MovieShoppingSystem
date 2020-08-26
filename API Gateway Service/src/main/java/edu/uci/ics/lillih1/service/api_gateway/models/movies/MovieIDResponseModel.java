package edu.uci.ics.lillih1.service.api_gateway.models.movies;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.lillih1.service.api_gateway.utilities.ResultCodes;

public class MovieIDResponseModel {
    private int resultCode;
    private String message;
    private MovieModel movie;

    @JsonCreator
    public MovieIDResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode,
                                @JsonProperty(value = "movie", required = true)  MovieModel movie) {
        this.resultCode = resultCode;
        this.message = ResultCodes.setMessage(resultCode);
        this.movie = movie;
    }

    @Override
    public String toString() {
        return "MovieIDResponseModel{" +
                "resultCode=" + resultCode +
                ", message='" + message + '\'' +
                ", movie=" + movie +
                '}';
    }

    @JsonProperty(value = "resultCode", required = true)
    public int getResultCode() {
        return resultCode;
    }

    @JsonProperty(value = "message", required = true)
    public String getMessage() {
        return message;
    }

    @JsonProperty(value = "movie", required = true)
    public MovieModel getMovie() {
        return movie;
    }
}
