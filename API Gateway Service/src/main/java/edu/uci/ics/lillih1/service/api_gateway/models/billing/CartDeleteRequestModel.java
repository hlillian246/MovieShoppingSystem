package edu.uci.ics.lillih1.service.api_gateway.models.billing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.lillih1.service.api_gateway.models.RequestModel;

public class CartDeleteRequestModel extends RequestModel {

    private String email;
    private String movieId;

    @JsonCreator
    public CartDeleteRequestModel(@JsonProperty(value = "email",required = true) String email,
                                  @JsonProperty(value = "movieId",required = true) String movieId)
    {
        this.email = email;
        this.movieId = movieId;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("movieId")
    public String getMovieId() {
        return movieId;
    }
}
