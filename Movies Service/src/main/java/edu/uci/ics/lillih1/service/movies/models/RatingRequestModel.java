package edu.uci.ics.lillih1.service.movies.models;

public class RatingRequestModel {
    private String id;
    private double rating;

    public RatingRequestModel() {
    }

    public RatingRequestModel(String id, double rating) {
        this.id = id;
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
