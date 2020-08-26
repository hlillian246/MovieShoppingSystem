package edu.uci.ics.lillih1.service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class MovieAddRequestModel {
    private String title;
    private String director;
    private Integer year;
    private String backdrop_path;
    private Integer budget;
    private String overview;
    private String poster_path;
    private Integer revenue;
    private List<GenreModel> genres = new ArrayList<GenreModel>();

    public MovieAddRequestModel() {
    }

    @JsonCreator
    public MovieAddRequestModel(@JsonProperty(value = "title",required = true) String title,
                                @JsonProperty(value = "director",required = true ) String director,
                                @JsonProperty(value = "year", required = true) Integer year,
                                @JsonProperty(value = "backdrop_path") String backdrop_path,
                                @JsonProperty(value = "budget") Integer budget,
                                @JsonProperty(value = "overview") String overview,
                                @JsonProperty(value = "poster_path") String poster_path,
                                @JsonProperty(value = "revenue") Integer revenue,
                                @JsonProperty(value = "genres",required = true) List<GenreModel> genres) {
        this.title = title;
        this.year = year;
        this.director = director;
        this.backdrop_path = backdrop_path;
        this.budget = budget;
        this.poster_path = poster_path;
        this.overview = overview;
        this.revenue = revenue;
        this.genres = genres;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public Integer getBudget() {
        return budget;
    }

    public void setBudget(Integer budget) {
        this.budget = budget;
    }

    public Integer getRevenue() {
        return revenue;
    }

    public void setRevenue(Integer revenue) {
        this.revenue = revenue;
    }

    public List<GenreModel> getGenres() {
        return genres;
    }

    public void setGenres(List<GenreModel> genres) {
        this.genres = genres;
    }
}
