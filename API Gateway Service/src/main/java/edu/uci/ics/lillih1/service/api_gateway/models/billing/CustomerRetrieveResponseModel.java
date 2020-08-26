package edu.uci.ics.lillih1.service.api_gateway.models.billing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.lillih1.service.api_gateway.utilities.ResultCodes;

public class CustomerRetrieveResponseModel {
    private int resultCode;
    private String message;
    private CustomerModel customer;

    @JsonCreator
    public CustomerRetrieveResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode,
                                         @JsonProperty(value = "resultCode", required = true) CustomerModel customer) {
        this.resultCode = resultCode;
        this.message = ResultCodes.setMessage(resultCode);
        this.customer = customer;
    }

    @JsonProperty(value = "resultCode", required = true)
    public int getResultCode() {
        return resultCode;
    }

    @JsonProperty(value = "message", required = true)
    public String getMessage() {
        return message;
    }

    @JsonProperty(value = "customer",required = true)
    public CustomerModel getCustomer() {
        return customer;
    }
}
