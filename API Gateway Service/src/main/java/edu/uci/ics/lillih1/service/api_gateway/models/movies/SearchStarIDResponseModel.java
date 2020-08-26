package edu.uci.ics.lillih1.service.api_gateway.models.movies;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.lillih1.service.api_gateway.utilities.ResultCodes;

import java.util.List;

public class SearchStarIDResponseModel {
    private int resultCode;
    private String message;
    private StarModel star;

    @JsonCreator
    public SearchStarIDResponseModel (@JsonProperty(value = "resultCode", required = true) int resultCode,
                                      @JsonProperty(value = "star", required = true) StarModel star ) {
        this.resultCode = resultCode;
        this.message = ResultCodes.setMessage(resultCode);
        this.star = star;
    }

    @Override
    public String toString() {
        return "SearchStarIDResponseModel{" +
                "resultCode=" + resultCode +
                ", message='" + message + '\'' +
                ", star=" + star +
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

    @JsonProperty(value = "star", required = true)
    public StarModel getStar() {
        return star;
    }
}
