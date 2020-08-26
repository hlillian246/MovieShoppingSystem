package edu.uci.ics.lillih1.service.idm.models;

import edu.uci.ics.lillih1.service.idm.core.Validate;

public class RegisterResponseModel implements Validate {
    private int resultCode;
    private String message;

    public RegisterResponseModel() {
    }

    public RegisterResponseModel(int resultCode, String message) {
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

    @Override
    public boolean isValid(){
        return true;
    }

}
