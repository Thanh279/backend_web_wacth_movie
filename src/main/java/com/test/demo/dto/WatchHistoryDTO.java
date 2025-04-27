package com.test.demo.dto;

import java.time.LocalDateTime;

public class WatchHistoryDTO {

    private String id;
    private String seriesId;
    private String title;
    private Integer seasonNumber;
    private Integer episodeNumber;
    private String posterPath;
    private LocalDateTime watchedAt;

    // Constructors
    public WatchHistoryDTO() {}

    public WatchHistoryDTO(String id, String seriesId, String title, Integer seasonNumber, 
                         Integer episodeNumber, String posterPath, LocalDateTime watchedAt) {
        this.id = id;
        this.seriesId = seriesId;
        this.title = title;
        this.seasonNumber = seasonNumber;
        this.episodeNumber = episodeNumber;
        this.posterPath = posterPath;
        this.watchedAt = watchedAt;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(String seriesId) {
        this.seriesId = seriesId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(Integer seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public Integer getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(Integer episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public LocalDateTime getWatchedAt() {
        return watchedAt;
    }

    public void setWatchedAt(LocalDateTime watchedAt) {
        this.watchedAt = watchedAt;
    }
}