package edu.uci.ics.lillih1.service.billing.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartRetrieveResponseModel {
    private int resultCode;
    private String message;
    private List<CartItemModel> items;

    public CartRetrieveResponseModel() {
    }

    public CartRetrieveResponseModel(int resultCode, String message) {
        this.resultCode = resultCode;
        this.message = message;
    }

    public CartRetrieveResponseModel(int resultCode, String message, List<CartItemModel> items) {
        this.resultCode = resultCode;
        this.message = message;
        this.items = items;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CartItemModel> getItems() {
        return items;
    }

    public void setItems(List<CartItemModel> items) {
        this.items = items;
    }
}
