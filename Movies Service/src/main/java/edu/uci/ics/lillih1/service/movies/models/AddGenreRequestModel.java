package edu.uci.ics.lillih1.service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AddGenreRequestModel {

    private String name;

    public AddGenreRequestModel() {
    }

    @JsonCreator
    public AddGenreRequestModel(@JsonProperty(value = "name", required = true) String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
