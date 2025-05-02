package com.test.demo.dto;

public class CreateGenreDTO {
    private String name;
    private Integer tmdbGenreId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTmdbGenreId() {
        return tmdbGenreId;
    }

    public void setTmdbGenreId(Integer tmdbGenreId) {
        this.tmdbGenreId = tmdbGenreId;
    }
}