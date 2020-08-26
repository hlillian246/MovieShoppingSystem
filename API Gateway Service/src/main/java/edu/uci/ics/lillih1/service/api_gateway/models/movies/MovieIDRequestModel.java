package edu.uci.ics.lillih1.service.api_gateway.models.movies;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.lillih1.service.api_gateway.models.RequestModel;

public class MovieIDRequestModel  extends RequestModel {

    private String email;
    private String sessionID;

    @JsonCreator
    public MovieIDRequestModel(@JsonProperty(value = "email",required = true) String email,
                               @JsonProperty(value = "sessionID",required = true) String sessionID) {
        this.email = email;
        this.sessionID = sessionID;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("sessionID")
    public String getSessionID() {
        return sessionID;
    }
}
