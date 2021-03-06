package edu.uci.ics.lillih1.service.api_gateway.models.billing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.lillih1.service.api_gateway.models.RequestModel;

public class CartRetrieveRequestModel extends RequestModel {
    private String email;

    @JsonCreator
    public CartRetrieveRequestModel(@JsonProperty(value = "email",required = true) String email)
    {
        this.email = email;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }
}
