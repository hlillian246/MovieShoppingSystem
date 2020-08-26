package edu.uci.ics.lillih1.service.idm.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.lillih1.service.idm.core.Validate;

public class PrivilegeRequestModel implements Validate {
    private String email;
    private int plevel;

    public PrivilegeRequestModel() {
    }
    @JsonCreator
    public PrivilegeRequestModel(@JsonProperty(value = "email",required = true) String email,
                                 @JsonProperty(value = "plevel",required = true) int plevel) {
        this.email = email;
        this.plevel = plevel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPlevel() {
        return plevel;
    }

    public void setPlevel(int plevel) {
        this.plevel = plevel;
    }

    @Override
    public boolean isValid()
    {
        return(email!= null && !email.isEmpty() && plevel>0 && plevel < 6);
    }
}
