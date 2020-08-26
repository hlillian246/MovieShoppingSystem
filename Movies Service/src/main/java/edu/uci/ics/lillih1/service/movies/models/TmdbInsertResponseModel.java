package edu.uci.ics.lillih1.service.movies.models;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TmdbInsertResponseModel {

    private int nummovies;
    private int numInserted;
    private int numUpdated;

    public TmdbInsertResponseModel() {
    }

    public TmdbInsertResponseModel(int nummovies, int numInserted, int numUpdated) {
        this.nummovies = nummovies;
        this.numInserted = numInserted;
        this.numUpdated = numUpdated;
    }

    public int getNummovies() {
        return nummovies;
    }

    public void setNummovies(int nummovies) {
        this.nummovies = nummovies;
    }

    public int getNumInserted() {
        return numInserted;
    }

    public void setNumInserted(int numInserted) {
        this.numInserted = numInserted;
    }

    public int getNumUpdated() {
        return numUpdated;
    }

    public void setNumUpdated(int numUpdated) {
        this.numUpdated = numUpdated;
    }
}
