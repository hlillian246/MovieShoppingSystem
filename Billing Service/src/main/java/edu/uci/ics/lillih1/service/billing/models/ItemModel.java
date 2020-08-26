package edu.uci.ics.lillih1.service.billing.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemModel
{
    private String email;
    private String movieId;
    private Integer quantity;
    private float unit_price;
    private float discount;

    public ItemModel() {
    }

    @JsonCreator
    public ItemModel(String email, String movieId, Integer quantity, float unit_price, float discount) {
        this.email = email;
        this.movieId = movieId;
        this.quantity = quantity;
        this.unit_price = unit_price;
        this.discount = discount;
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

    public float getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(float unit_price) {
        this.unit_price = unit_price;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }
}
