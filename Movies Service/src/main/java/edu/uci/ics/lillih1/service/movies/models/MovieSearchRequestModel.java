package edu.uci.ics.lillih1.service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MovieSearchRequestModel {
    private String title;
    private String genre;
    private Integer year;
    private String director;
    private Boolean includeHidden;
    private Integer limit;
    private  Integer offset;
    private String orderby;
    private String direction;

    public MovieSearchRequestModel() {
    }

    @JsonCreator
    public MovieSearchRequestModel(@JsonProperty(value = "title") String title,
                                   @JsonProperty(value = "genre") String genre,
                                   @JsonProperty(value = "year")Integer year,
                                   @JsonProperty(value = "director")String director,
                                   @JsonProperty(value = "hidden") Boolean includeHidden,
                                   @JsonProperty(value = "limit") Integer limit,
                                   @JsonProperty(value = "offset") Integer offset,
                                   @JsonProperty(value = "orderby") String orderby,
                                   @JsonProperty(value = "direction") String direction) {
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.director = director;
        this.includeHidden = includeHidden;
        this.limit = limit;
        this.offset = offset;
        this.orderby = orderby;
        this.direction = direction;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public Boolean getIncludeHidden() {
        return includeHidden;
    }

    public void setIncludeHidden(Boolean includeHidden) {
        this.includeHidden = includeHidden;
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
