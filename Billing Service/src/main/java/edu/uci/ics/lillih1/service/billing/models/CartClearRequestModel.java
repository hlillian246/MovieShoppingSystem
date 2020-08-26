package edu.uci.ics.lillih1.service.billing.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CartClearRequestModel
{
    private String email;

    public CartClearRequestModel() {
    }

    @JsonCreator
    public CartClearRequestModel(@JsonProperty(value = "email",required = true) String email)
    {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
