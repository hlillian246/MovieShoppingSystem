package edu.uci.ics.lillih1.service.billing.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import edu.uci.ics.lillih1.service.billing.utils.LocalDateDeserializer;
import edu.uci.ics.lillih1.service.billing.utils.LocalDateSerializer;

import java.time.LocalDate;

public class CreditCardInsertRequestModel
{
    private String id;
    private String firstName;
    private String lastName;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate expiration;

    public CreditCardInsertRequestModel() {
    }

    @JsonCreator
    public CreditCardInsertRequestModel(@JsonProperty(value = "id",required = true) String id,
                                        @JsonProperty(value = "firstName",required = true) String firstName,
                                        @JsonProperty(value = "lastName",required = true) String lastName,
                                        @JsonProperty(value = "expiration",required = true) LocalDate expiration)
    {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.expiration = expiration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getExpiration() {
        return expiration;
    }

    public void setExpiration(LocalDate expiration) {
        this.expiration = expiration;
    }
}
