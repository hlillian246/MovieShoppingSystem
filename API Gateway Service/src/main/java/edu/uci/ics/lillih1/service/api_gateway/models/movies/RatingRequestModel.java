package edu.uci.ics.lillih1.service.api_gateway.models.movies;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.lillih1.service.api_gateway.models.RequestModel;

public class RatingRequestModel  extends RequestModel {
    private String id;
    private double rating;

    @JsonCreator
    public RatingRequestModel(@JsonProperty(value = "id",required = true) String id,
                              @JsonProperty(value = "rating",required = true) double rating) {
        this.id = id;
        this.rating = rating;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("rating")
    public double getRating() {
        return rating;
    }
}
