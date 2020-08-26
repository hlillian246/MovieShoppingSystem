package edu.uci.ics.lillih1.service.movies.models;

public class StarInRequestModel {
    private String starid;
    private String movieid;

    public StarInRequestModel() {
    }

    public StarInRequestModel(String starid, String movieid) {
        this.starid = starid;
        this.movieid = movieid;
    }

    public String getStarid() {
        return starid;
    }

    public void setStarid(String starid) {
        this.starid = starid;
    }

    public String getMovieid() {
        return movieid;
    }

    public void setMovieid(String movieid) {
        this.movieid = movieid;
    }
}
