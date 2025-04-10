package com.test.demo.service.serviceimpl;

import com.test.demo.dto.EpisodeCreateDTO;
import com.test.demo.entity.Animation;
import com.test.demo.entity.Episode;
import com.test.demo.reponsitory.AnimationRepository;
import com.test.demo.reponsitory.EpisodeRepository;
import com.test.demo.service.EpisodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class EpisodeServiceImpl implements EpisodeService {
    @Autowired
    private EpisodeRepository episodeRepository;

    @Autowired
    private AnimationRepository animationRepository;

    @Override
    public Page<Episode> getAllEpisodes(Pageable pageable) {
        return episodeRepository.findAll(pageable);
    }

    @Override
    public Episode getEpisodeById(Long id) {
        return episodeRepository.findById(id).orElseThrow(() -> new RuntimeException("Episode not found"));
    }

    @Override
    public Episode createEpisode(EpisodeCreateDTO dto) {
        Animation animation = animationRepository.findById(dto.getAnimationId())
                .orElseThrow(() -> new RuntimeException("Animation not found"));

        Episode episode = new Episode();
        episode.setAnimation(animation);
        episode.setEpisodeNumber(dto.getEpisodeNumber());
        episode.setTitle(dto.getTitle());
        episode.setReleaseDate(dto.getReleaseDate() != null ? LocalDate.parse(dto.getReleaseDate()) : null);
        episode.setVideoUrl(dto.getVideoUrl());

        return episodeRepository.save(episode);
    }

    @Override
    public Episode updateEpisode(Long id, Episode episode) {
        Episode existing = getEpisodeById(id);
        existing.setAnimation(episode.getAnimation() != null ? episode.getAnimation() : existing.getAnimation());
        existing.setEpisodeNumber(episode.getEpisodeNumber() != null ? episode.getEpisodeNumber() : existing.getEpisodeNumber());
        existing.setTitle(episode.getTitle() != null ? episode.getTitle() : existing.getTitle());
        existing.setReleaseDate(episode.getReleaseDate() != null ? episode.getReleaseDate() : existing.getReleaseDate());
        existing.setVideoUrl(episode.getVideoUrl() != null ? episode.getVideoUrl() : existing.getVideoUrl());
        return episodeRepository.save(existing);
    }

    @Override
    public void deleteEpisode(Long id) {
        episodeRepository.deleteById(id);
    }
}