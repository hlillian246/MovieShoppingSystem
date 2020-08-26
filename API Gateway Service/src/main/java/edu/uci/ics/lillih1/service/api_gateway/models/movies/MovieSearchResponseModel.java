package edu.uci.ics.lillih1.service.api_gateway.models.movies;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.lillih1.service.api_gateway.utilities.ResultCodes;

import java.util.List;

public class MovieSearchResponseModel {
    private int resultCode;
    private String message;
    private List<MovieModel> movies;

    @JsonCreator
    public MovieSearchResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode,List<MovieModel> movies) {
        this.resultCode = resultCode;
        this.message = ResultCodes.setMessage(resultCode);
        this.movies = movies;
    }

    @Override
    public String toString() {
        return "MovieSearchResponseModel{" +
                "resultCode=" + resultCode +
                ", message='" + message + '\'' +
                ", movies=" + movies +
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

    @JsonProperty(value = "movies", required = true)
    public List<MovieModel> getMovies() {
        return movies;
    }
}
