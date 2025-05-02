package com.test.demo.dto;

import java.time.LocalDateTime;

public class FavoriteDTO {
    private String id;
    private String seriesId;
    private String title;
    private String posterPath;
    private LocalDateTime addedAt;

    public FavoriteDTO(String id, String seriesId, String title, String posterPath, LocalDateTime addedAt) {
        this.id = id;
        this.seriesId = seriesId;
        this.title = title;
        this.posterPath = posterPath;
        this.addedAt = addedAt;
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

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }
}