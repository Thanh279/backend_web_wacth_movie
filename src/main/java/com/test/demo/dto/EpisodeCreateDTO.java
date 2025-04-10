package com.test.demo.dto;



public class EpisodeCreateDTO {
    private Long id;
    private Long animationId; 
    private Integer episodeNumber;
    private String title;
    private String releaseDate;
    private String videoUrl;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getAnimationId() { return animationId; }
    public void setAnimationId(Long animationId) { this.animationId = animationId; }

    public Integer getEpisodeNumber() { return episodeNumber; }
    public void setEpisodeNumber(Integer episodeNumber) { this.episodeNumber = episodeNumber; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getReleaseDate() { return releaseDate; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }

    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }
}