package com.test.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "genres")
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "normalized_name", nullable = false)
    private String normalizedName;

    @Column(name = "tmdb_genre_id")
    private Integer tmdbGenreId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNormalizedName() {
        return normalizedName;
    }

    public void setNormalizedName(String normalizedName) {
        this.normalizedName = normalizedName;
    }

    public Integer getTmdbGenreId() {
        return tmdbGenreId;
    }

    public void setTmdbGenreId(Integer tmdbGenreId) {
        this.tmdbGenreId = tmdbGenreId;
    }
}