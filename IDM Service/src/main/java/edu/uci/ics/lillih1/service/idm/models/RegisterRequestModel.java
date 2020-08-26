package edu.uci.ics.lillih1.service.idm.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.lillih1.service.idm.core.Validate;
import edu.uci.ics.lillih1.service.idm.logger.ServiceLogger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterRequestModel implements Validate {

    private String email;
    private char[] password;

    public RegisterRequestModel() {
    }

    @JsonCreator
    public RegisterRequestModel(@JsonProperty(value = "email",required = true)String email,
                                @JsonProperty(value = "password",required = true)char[] password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    @Override
    public boolean isValid()
    {
       return true;
    }
}
