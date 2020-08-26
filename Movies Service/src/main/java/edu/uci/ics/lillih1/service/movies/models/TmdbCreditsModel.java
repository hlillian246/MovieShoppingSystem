package edu.uci.ics.lillih1.service.movies.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TmdbCreditsModel {
    private List<TmdbCastModel> cast;
    private List<TmdbCrewModel> crew;

    public TmdbCreditsModel() {
    }

    public List<TmdbCastModel> getCast() {
        return cast;
    }

    public void setCast(List<TmdbCastModel> cast) {
        this.cast = cast;
    }

    public List<TmdbCrewModel> getCrew() {
        return crew;
    }

    public void setCrew(List<TmdbCrewModel> crew) {
        this.crew = crew;
    }
}
