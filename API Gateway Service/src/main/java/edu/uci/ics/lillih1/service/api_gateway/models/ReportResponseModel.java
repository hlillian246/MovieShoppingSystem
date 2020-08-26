package edu.uci.ics.lillih1.service.api_gateway.models;

public class ReportResponseModel {

    private int resultCode;
    private String message;
    private String response;


    public ReportResponseModel(String message) {
        this.message = message;
    }

    public ReportResponseModel(int resultCode, String message, String response) {
        this.resultCode = resultCode;
        this.message = message;
        this.response = response;
    }

    public ReportResponseModel(int resultCode, String message) {
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

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
