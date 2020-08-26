package edu.uci.ics.lillih1.service.api_gateway.models.billing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import edu.uci.ics.lillih1.service.api_gateway.models.RequestModel;
import edu.uci.ics.lillih1.service.billing.utils.LocalDateDeserializer;
import edu.uci.ics.lillih1.service.billing.utils.LocalDateSerializer;

import java.time.LocalDate;

public class CreditCardUpdateRequestModel extends RequestModel {

    private String id;
    private String firstName;
    private String lastName;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate expiration;


    @JsonCreator
    public CreditCardUpdateRequestModel(@JsonProperty(value = "id",required = true) String id,
                                        @JsonProperty(value = "firstName",required = true) String firstName,
                                        @JsonProperty(value = "lastName",required = true) String lastName,
                                        @JsonProperty(value = "expiration",required = true) LocalDate expiration)
    {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.expiration = expiration;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("firstName")
    public String getFirstName() {
        return firstName;
    }

    @JsonProperty("lastName")
    public String getLastName() {
        return lastName;
    }

    @JsonProperty("expiration")
    public LocalDate getExpiration() {
        return expiration;
    }
}
