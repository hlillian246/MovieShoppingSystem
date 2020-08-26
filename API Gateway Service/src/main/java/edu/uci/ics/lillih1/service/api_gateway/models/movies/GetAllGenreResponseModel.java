package edu.uci.ics.lillih1.service.api_gateway.models.movies;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.lillih1.service.api_gateway.utilities.ResultCodes;

import java.util.ArrayList;
import java.util.List;

public class GetAllGenreResponseModel {
    private int resultCode;
    private String message;
    private List<GenreModel> genres = new ArrayList<GenreModel>();

    @JsonCreator
    public GetAllGenreResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode,
                                    @JsonProperty(value = "genres", required = true) List<GenreModel> genres ) {
        this.resultCode = resultCode;
        this.message = ResultCodes.setMessage(resultCode);
        this.genres = genres;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "[resultCode: " + resultCode + ", message: " + message + "]";
    }

    @JsonProperty(value = "resultCode", required = true)
    public int getResultCode() {
        return resultCode;
    }

    @JsonProperty(value = "message", required = true)
    public String getMessage() {
        return message;
    }

    @JsonProperty(value = "genres", required = true)
    public List<GenreModel> getGenres() {
        return genres;
    }
}
