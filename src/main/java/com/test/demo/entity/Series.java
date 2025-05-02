package com.test.demo.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "series")
public class Series {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dbId;

    @Column(name = "tmdb_id", unique = true, nullable = false)
    private Long tmdbId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "vote_average")
    private Double voteAverage;

    @Column(name = "first_air_date")
    private String firstAirDate;

    @Column(name = "poster_path")
    private String posterPath;

    @Column(name = "genre_ids", columnDefinition = "TEXT") // Lưu dưới dạng JSON string
    private String genreIds; // Ví dụ: "[18, 35]"

    // Getters and Setters
    public Long getDbId() {
        return dbId;
    }

    public void setDbId(Long dbId) {
        this.dbId = dbId;
    }

    public Long getTmdbId() {
        return tmdbId;
    }

    public void setTmdbId(Long tmdbId) {
        this.tmdbId = tmdbId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public void setFirstAirDate(String firstAirDate) {
        this.firstAirDate = firstAirDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(String genreIds) {
        this.genreIds = genreIds;
    }
}