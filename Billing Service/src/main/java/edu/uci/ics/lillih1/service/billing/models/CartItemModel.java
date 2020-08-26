package edu.uci.ics.lillih1.service.billing.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartItemModel
{
    private String email;
    private String movieId;
    private Integer quantity;

    public CartItemModel() {
    }

    @JsonCreator
    public CartItemModel(String email,
                         String movieId,
                         Integer quantity)
    {
        this.email = email;
        this.movieId = movieId;
        this.quantity = quantity;
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
