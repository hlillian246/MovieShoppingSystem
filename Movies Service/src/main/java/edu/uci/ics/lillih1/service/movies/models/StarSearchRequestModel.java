package edu.uci.ics.lillih1.service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StarSearchRequestModel {
    private String name;
    private Integer birthYear;
    private String movieTitle;
    private Integer limit;
    private Integer offset;
    private String orderby;
    private String direction;

    public StarSearchRequestModel() {
    }

    @JsonCreator
    public StarSearchRequestModel(@JsonProperty(value = "name") String name,
                                  @JsonProperty(value = "birthYear") Integer birthYear,
                                  @JsonProperty(value = "movieTitle") String movieTitle,
                                  @JsonProperty(value = "limit") Integer limit,
                                  @JsonProperty(value = "offset") Integer offset,
                                  @JsonProperty(value = "orderby") String orderby,
                                  @JsonProperty(value = "direction") String direction) {
        this.name = name;
        this.birthYear = birthYear;
        this.movieTitle = movieTitle;
        this.limit = limit;
        this.offset = offset;
        this.orderby = orderby;
        this.direction = direction;
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

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public String getOrderby() {
        return orderby;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
