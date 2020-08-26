package edu.uci.ics.lillih1.service.billing.models;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerRetrieveResponseModel {

    private int resultCode;
    private String message;
    private CustomerModel customer;

    public CustomerRetrieveResponseModel() {
    }

    public CustomerRetrieveResponseModel(int resultCode, String message) {
        this.resultCode = resultCode;
        this.message = message;
    }

    public CustomerRetrieveResponseModel(int resultCode, String message, CustomerModel customer) {
        this.resultCode = resultCode;
        this.message = message;
        this.customer = customer;
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

    public CustomerModel getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerModel customer) {
        this.customer = customer;
    }
}
