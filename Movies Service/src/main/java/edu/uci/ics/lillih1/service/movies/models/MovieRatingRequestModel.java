package edu.uci.ics.lillih1.service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MovieRatingRequestModel {
    private String id;
    private double rating;

    public MovieRatingRequestModel() {
    }

    @JsonCreator
    public MovieRatingRequestModel(@JsonProperty(value = "id", required = true) String id,
                                   @JsonProperty(value = "rating", required = true) double rating) {
        this.id = id;
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
