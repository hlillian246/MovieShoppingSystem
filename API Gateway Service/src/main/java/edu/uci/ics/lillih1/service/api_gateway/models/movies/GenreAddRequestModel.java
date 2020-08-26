package edu.uci.ics.lillih1.service.api_gateway.models.movies;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.lillih1.service.api_gateway.models.RequestModel;

public class GenreAddRequestModel  extends RequestModel {
    private String name;

    @JsonCreator
    public GenreAddRequestModel(@JsonProperty(value = "name",required = true) String name) {
        this.name = name;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }
}
