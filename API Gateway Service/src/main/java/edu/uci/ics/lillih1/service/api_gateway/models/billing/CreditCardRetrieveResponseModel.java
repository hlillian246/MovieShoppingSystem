package edu.uci.ics.lillih1.service.api_gateway.models.billing;

public class CreditCardRetrieveResponseModel {
    private int resultCode;
    private String message;
    private CreditCardModel creditcard;

    public CreditCardRetrieveResponseModel(int resultCode, String message, CreditCardModel creditcard) {
        this.resultCode = resultCode;
        this.message = message;
        this.creditcard = creditcard;
    }

    public int getResultCode() {
        return resultCode;
    }

    public String getMessage() {
        return message;
    }

    public CreditCardModel getCreditcard() {
        return creditcard;
    }
}
