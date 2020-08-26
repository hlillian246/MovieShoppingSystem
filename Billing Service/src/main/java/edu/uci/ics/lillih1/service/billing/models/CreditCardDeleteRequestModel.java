package edu.uci.ics.lillih1.service.billing.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class CreditCardDeleteRequestModel
{
    private String id;

    public CreditCardDeleteRequestModel() {
    }

    @JsonCreator
    public CreditCardDeleteRequestModel(@JsonProperty(value = "id",required = true) String id)
    {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
