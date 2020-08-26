package edu.uci.ics.lillih1.service.movies.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TmdbGetAllResponseModel {

    private int nummovies;
    private Map<Integer, Integer> httpStatusCounts;

    public TmdbGetAllResponseModel() {
    }

    public TmdbGetAllResponseModel(int nummovies, Map<Integer, Integer> map) {
        this.nummovies = nummovies;
        this.httpStatusCounts = map;
    }

    public int getNummovies() {
        return nummovies;
    }

    public void setNummovies(int nummovies) {
        this.nummovies = nummovies;
    }

    public Map<Integer, Integer> getHttpStatusCounts() {
        return httpStatusCounts;
    }

    public void setHttpStatusCounts(Map<Integer, Integer> httpStatusCounts) {
        this.httpStatusCounts = httpStatusCounts;
    }
}
