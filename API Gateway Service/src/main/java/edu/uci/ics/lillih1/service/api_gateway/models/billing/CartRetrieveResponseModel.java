package edu.uci.ics.lillih1.service.api_gateway.models.billing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CartRetrieveResponseModel {
    private int resultCode;
    private String message;
    private List<CartItemModel> items;

    public CartRetrieveResponseModel(int resultCode, String message, List<CartItemModel> items) {
        this.resultCode = resultCode;
        this.message = message;
        this.items = items;
    }

    @JsonProperty(value = "resultCode", required = true)
    public int getResultCode() {
        return resultCode;
    }

    @JsonProperty(value = "message", required = true)
    public String getMessage() {
        return message;
    }

    @JsonProperty(value = "items", required = true)
    public List<CartItemModel> getItems() {
        return items;
    }
}
