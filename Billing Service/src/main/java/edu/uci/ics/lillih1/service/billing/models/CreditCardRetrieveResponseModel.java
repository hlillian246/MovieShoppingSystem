package edu.uci.ics.lillih1.service.billing.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.uci.ics.lillih1.service.billing.resources.CreditCard;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreditCardRetrieveResponseModel {
    private int resultCode;
    private String message;
    private CreditCardModel creditcard;

    public CreditCardRetrieveResponseModel() {
    }

    public CreditCardRetrieveResponseModel(int resultCode, String message) {
        this.resultCode = resultCode;
        this.message = message;
    }

    public CreditCardRetrieveResponseModel(int resultCode, String message, CreditCardModel creditcard) {
        this.resultCode = resultCode;
        this.message = message;
        this.creditcard = creditcard;
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

    public CreditCardModel getCreditcard() {
        return creditcard;
    }

    public void setCreditcard(CreditCardModel creditcard) {
        this.creditcard = creditcard;
    }
}
