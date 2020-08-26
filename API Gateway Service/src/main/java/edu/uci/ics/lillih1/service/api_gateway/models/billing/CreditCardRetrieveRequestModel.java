package edu.uci.ics.lillih1.service.api_gateway.models.billing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.lillih1.service.api_gateway.models.RequestModel;

public class CreditCardRetrieveRequestModel extends RequestModel {
    private String id;

    @JsonCreator
    public CreditCardRetrieveRequestModel(@JsonProperty(value = "id",required = true) String id)
    {
        this.id = id;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

}
