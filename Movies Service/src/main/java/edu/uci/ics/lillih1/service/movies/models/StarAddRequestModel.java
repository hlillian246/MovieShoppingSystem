package edu.uci.ics.lillih1.service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StarAddRequestModel {
    private String name;
    private Integer birthYear;

    public StarAddRequestModel() {
    }

    @JsonCreator
    public StarAddRequestModel(@JsonProperty(value = "name",required = true) String name,
                               @JsonProperty(value = "birthYear") Integer birthYear) {
        this.name = name;
        this.birthYear = birthYear;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }
}
