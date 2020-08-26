package edu.uci.ics.lillih1.service.billing.models;

import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreditCardDeleteResponseModel {
    private int resultCode;
    private String message;

    public CreditCardDeleteResponseModel() {
    }

    public CreditCardDeleteResponseModel(int resultCode, String message) {
        this.resultCode = resultCode;
        this.message = message;
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
}
