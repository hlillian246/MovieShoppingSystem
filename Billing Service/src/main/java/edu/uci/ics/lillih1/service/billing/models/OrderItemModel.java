package edu.uci.ics.lillih1.service.billing.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import edu.uci.ics.lillih1.service.billing.utils.LocalDateDeserializer;
import edu.uci.ics.lillih1.service.billing.utils.LocalDateSerializer;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderItemModel
{
    private String email;
    private String movieId;
    private Integer quantity;
    private Float unit_price;
    private Float discount;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate saleDate;

    public OrderItemModel() {
    }

    @JsonCreator
    public OrderItemModel(String email,
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

    public LocalDate getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDate saleDate) {
        this.saleDate = saleDate;
    }

    public Float getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(Float unit_price) {
        this.unit_price = unit_price;
    }

    public Float getDiscount() {
        return discount;
    }

    public void setDiscount(Float discount) {
        this.discount = discount;
    }
}
