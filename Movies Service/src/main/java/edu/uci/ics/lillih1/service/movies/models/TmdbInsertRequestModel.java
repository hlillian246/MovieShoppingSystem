package edu.uci.ics.lillih1.service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TmdbInsertRequestModel {
    private Integer start;
    private Integer limit;
    private Integer index;

    public TmdbInsertRequestModel() {
    }

    @JsonCreator
    public TmdbInsertRequestModel(@JsonProperty(value = "start", required = true) Integer start,
                                  @JsonProperty(value = "limit", required = true) Integer limit,
                                  @JsonProperty(value = "index", required = true) Integer index) {
        this.start = start;
        this.limit = limit;
        this.index = index;
    }


    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
