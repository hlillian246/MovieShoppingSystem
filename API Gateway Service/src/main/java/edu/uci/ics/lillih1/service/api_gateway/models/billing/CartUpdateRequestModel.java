package edu.uci.ics.lillih1.service.api_gateway.models.billing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.lillih1.service.api_gateway.models.RequestModel;

public class CartUpdateRequestModel extends RequestModel {
    private String email;
    private String movieId;
    private Integer quantity;

    @JsonCreator
    public CartUpdateRequestModel(@JsonProperty(value = "email",required = true) String email,
                                  @JsonProperty(value = "movieId",required = true) String movieId,
                                  @JsonProperty(value = "quantity", required = true) Integer quantity)
    {
        this.email = email;
        this.movieId = movieId;
        this.quantity = quantity;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("movieId")
    public String getMovieId() {
        return movieId;
    }

    @JsonProperty("quantity")
    public Integer getQuantity() {
        return quantity;
    }
}
