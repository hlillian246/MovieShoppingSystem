package edu.uci.ics.lillih1.service.api_gateway.models.billing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.lillih1.service.api_gateway.utilities.ResultCodes;

import java.util.List;

public class OrderRetrieveResponseModel {

    private int resultCode;
    private String message;
    private List<TransactionModel> transactions;


    @JsonCreator
    public OrderRetrieveResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode,
                                      @JsonProperty(value = "transactions",required = true) List<TransactionModel> transactions) {
        this.resultCode = resultCode;
        this.message = ResultCodes.setMessage(resultCode);
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "[resultCode: " + resultCode + ", message: " + message + "]";
    }

    @JsonProperty(value = "resultCode", required = true)
    public int getResultCode() {
        return resultCode;
    }

    @JsonProperty(value = "message", required = true)
    public String getMessage() {
        return message;
    }
}
