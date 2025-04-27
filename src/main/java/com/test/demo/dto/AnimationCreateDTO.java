package com.test.demo.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.test.demo.entity.Animation.AnimationType;

import jakarta.persistence.Column;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AnimationCreateDTO {
    @NotBlank(message = "Title is mandatory")
    private String title;

    private Integer releaseYear;

    private String description;

    @NotNull(message = "Director ID is mandatory")
    private Long directorId;

    @NotNull(message = "Genre ID is mandatory")
    private List<Long> genreId;

    @NotNull(message = "Studio ID is mandatory")
    private Long studioId;

    private String videoUrl;

    @NotNull(message = "Type is mandatory")
    private AnimationType type;

    private String imgUrl;
   @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Transient
    private boolean isUpdated;
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    // Getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getDirectorId() {
        return directorId;
    }

    public void setDirectorId(Long directorId) {
        this.directorId = directorId;
    }

    public List<Long> getGenreId() {
        return genreId;
    }

    public void setGenreId(List<Long> genreId) {
        this.genreId = genreId;
    }

    public Long getStudioId() {
        return studioId;
    }

    public void setStudioId(Long studioId) {
        this.studioId = studioId;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public AnimationType getType() {
        return type;
    }

    public void setType(AnimationType type) {
        this.type = type;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}