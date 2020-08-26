package edu.uci.ics.lillih1.service.billing.models;


import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderPlaceResponseModel {
    private int resultCode;
    private String message;
    private String redirectURL;
    private String token;

    public OrderPlaceResponseModel() {
    }

    public OrderPlaceResponseModel(int resultCode, String message) {
        this.resultCode = resultCode;
        this.message = message;
    }

    public OrderPlaceResponseModel(int resultCode, String message, String redirectURL, String token) {
        this.resultCode = resultCode;
        this.message = message;
        this.redirectURL = redirectURL;
        this.token = token;
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

    public String getRedirectURL() {
        return redirectURL;
    }

    public void setRedirectURL(String redirectURL) {
        this.redirectURL = redirectURL;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
