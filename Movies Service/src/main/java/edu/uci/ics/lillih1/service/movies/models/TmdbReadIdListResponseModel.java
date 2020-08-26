package edu.uci.ics.lillih1.service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TmdbReadIdListResponseModel {

    private int nummovies;

    public TmdbReadIdListResponseModel() {
    }

    public int getNummovies() {
        return nummovies;
    }

    public void setNummovies(int nummovies) {
        this.nummovies = nummovies;
    }
}
