package edu.uci.ics.lillih1.service.billing.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreditCardRetrieveRequestModel
{
    private String id;

    public CreditCardRetrieveRequestModel() {
    }

    @JsonCreator
    public CreditCardRetrieveRequestModel(@JsonProperty(value = "id",required = true) String id)
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
