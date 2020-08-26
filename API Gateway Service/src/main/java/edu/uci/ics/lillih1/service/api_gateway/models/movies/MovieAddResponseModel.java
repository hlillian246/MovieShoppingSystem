package edu.uci.ics.lillih1.service.api_gateway.models.movies;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.lillih1.service.api_gateway.utilities.ResultCodes;

import java.util.List;

public class MovieAddResponseModel {
    private int resultCode;
    private String message;
    private String movieid;
    private List<Integer> genreid;

    @JsonCreator
    public MovieAddResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode,
                                 @JsonProperty(value = "movieid", required = true) String movieid,
                                 @JsonProperty(value = "genreid", required = true) List<Integer> genreid) {
        this.resultCode = resultCode;
        this.message = ResultCodes.setMessage(resultCode);
        this.movieid = movieid;
        this.genreid = genreid;
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

    @JsonProperty(value = "movieid", required = true)
    public String getMovieid() {
        return movieid;
    }

    @JsonProperty(value = "genreid", required = true)
    public List<Integer> getGenreid() {
        return genreid;
    }
}
