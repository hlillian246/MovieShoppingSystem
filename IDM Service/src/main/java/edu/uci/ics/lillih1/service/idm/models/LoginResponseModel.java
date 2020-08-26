package edu.uci.ics.lillih1.service.idm.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.lillih1.service.idm.core.Validate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponseModel implements Validate {
    private int resultCode;
    private String message;
    private String sessionID;

    public LoginResponseModel() {
    }

    public LoginResponseModel(int resultCode, String message) {
        this.resultCode = resultCode;
        this.message = message;
    }

    @JsonCreator
    public LoginResponseModel(@JsonProperty(value = "resultCode",required = true)int resultCode,
                              @JsonProperty(value = "message",required = true) String message,
                              @JsonProperty(value = "sessionID") String sessionID) {
        this.resultCode = resultCode;
        this.message = message;
        this.sessionID = sessionID;
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

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    @Override
    public boolean isValid(){
        return true;
    }


}
