package edu.uci.ics.lillih1.service.billing.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderPlaceRequestModel {

    private String email;

    public OrderPlaceRequestModel() {
    }

    public OrderPlaceRequestModel(@JsonProperty(value = "email", required = true) String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
