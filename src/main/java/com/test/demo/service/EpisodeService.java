package com.test.demo.service;

import com.test.demo.dto.EpisodeCreateDTO;
import com.test.demo.entity.Episode;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EpisodeService {
    List<Episode> getAllEpisodes();

    Episode getEpisodeById(Long id);

    Episode createEpisode(EpisodeCreateDTO dto);

    Episode updateEpisode(Long id, Episode episode);

    void deleteEpisode(Long id);
}