package edu.uci.ics.lillih1.service.api_gateway.models.idm;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.lillih1.service.api_gateway.models.RequestModel;

public class PrivilegeRequestModel  extends RequestModel {

    private String email;
    private int plevel;

    @JsonCreator
    public PrivilegeRequestModel(@JsonProperty(value = "email", required = true) String email,
                                 @JsonProperty(value = "plevel", required = true) int plevel) {
        this.email = email;
        this.plevel = plevel;
    }


    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("plevel")
    public int getPlevel() {
        return plevel;
    }

    @Override
    public String toString() {
        return "PrivilegeRequestModel{" +
                "email='" + email + '\'' +
                ", plevel=" + plevel +
                '}';
    }
}
