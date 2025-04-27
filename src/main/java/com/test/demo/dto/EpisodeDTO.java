package com.test.demo.dto;

public class EpisodeDTO {
    private int episodeNumber;
    private String videoUrl;

    public EpisodeDTO(int episodeNumber, String videoUrl) {
        this.episodeNumber = episodeNumber;
        this.videoUrl = videoUrl;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public String getVideoUrl() {
        return videoUrl;
    }
}