package edu.uci.ics.lillih1.service.api_gateway.models.movies;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.lillih1.service.api_gateway.utilities.ResultCodes;

import java.util.List;

public class StarSearchResponseModel {
    private int resultCode;
    private String message;
    private List<StarModel> stars;

    @JsonCreator
    public StarSearchResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode,
                                   @JsonProperty(value = "stars", required = true) List<StarModel> stars) {
        this.resultCode = resultCode;
        this.message = ResultCodes.setMessage(resultCode);
        this.stars = stars;
    }

    @Override
    public String toString() {
        return "StarSearchResponseModel{" +
                "resultCode=" + resultCode +
                ", message='" + message + '\'' +
                ", stars=" + stars +
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

    @JsonProperty(value = "stars", required = true)
    public List<StarModel> getStars() {
        return stars;
    }
}
