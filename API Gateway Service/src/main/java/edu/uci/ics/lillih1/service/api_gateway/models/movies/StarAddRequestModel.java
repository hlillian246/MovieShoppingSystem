package edu.uci.ics.lillih1.service.api_gateway.models.movies;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.lillih1.service.api_gateway.models.RequestModel;

public class StarAddRequestModel extends RequestModel {
    private String name;
    private Integer birthYear;

    @JsonCreator
    public StarAddRequestModel(@JsonProperty(value = "name",required = true) String name,
                               @JsonProperty(value = "birthYear") Integer birthYear) {
        this.name = name;
        this.birthYear = birthYear;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("birthYear")
    public Integer getBirthYear() {
        return birthYear;
    }
}
