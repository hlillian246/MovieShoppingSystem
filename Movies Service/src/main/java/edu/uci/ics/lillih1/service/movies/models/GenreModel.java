package edu.uci.ics.lillih1.service.movies.models;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenreModel {
    private int id;
    private String name;

    public GenreModel() {
    }

    public GenreModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof GenreModel)) {
            return false;
        }

        GenreModel g = (GenreModel) o;
        return this.id == g.id && this.name.equals(g.getName());
    }
}