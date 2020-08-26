package edu.uci.ics.lillih1.service.api_gateway.models.movies;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.lillih1.service.api_gateway.models.RequestModel;

public class StarInRequestModel  extends RequestModel {
    private String starid;
    private String movieid;

    @JsonCreator
    public StarInRequestModel(@JsonProperty(value = "starid",required = true) String starid,
                              @JsonProperty(value = "movieid",required = true) String movieid) {
        this.starid = starid;
        this.movieid = movieid;
    }

    @JsonProperty("starid")
    public String getStarid() {
        return starid;
    }

    @JsonProperty("movieid")
    public String getMovieid() {
        return movieid;
    }
}
