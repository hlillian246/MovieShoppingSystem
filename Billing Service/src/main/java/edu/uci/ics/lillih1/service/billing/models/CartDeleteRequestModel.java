package edu.uci.ics.lillih1.service.billing.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CartDeleteRequestModel
{
    private String email;
    private String movieId;

    public CartDeleteRequestModel() {
    }

    @JsonCreator
    public CartDeleteRequestModel(@JsonProperty(value = "email",required = true) String email,
                                  @JsonProperty(value = "movieId",required = true) String movieId)
    {
        this.email = email;
        this.movieId = movieId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }
}
