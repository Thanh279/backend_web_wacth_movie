package com.test.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "watch_history", uniqueConstraints = @UniqueConstraint(columnNames = { "username", "seriesId",
        "seasonNumber", "episodeNumber" }))
public class WatchHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String seriesId;

    @Column(nullable = false)
    private String title;

    @Column
    private Integer seasonNumber;

    @Column
    private Integer episodeNumber;

    private String posterPath;

    @Column(nullable = false)
    private LocalDateTime watchedAt;

    // Constructors
    public WatchHistoryEntity() {
        this.watchedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
        this.seasonNumber = seasonNumber != null && seasonNumber > 0 ? seasonNumber : 1;
    }

    public Integer getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(Integer episodeNumber) {
        this.episodeNumber = episodeNumber != null && episodeNumber > 0 ? episodeNumber : 1;
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