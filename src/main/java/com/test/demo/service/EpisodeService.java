package com.test.demo.service;

import com.test.demo.dto.EpisodeCreateDTO;
import com.test.demo.entity.Episode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EpisodeService {
    Page<Episode> getAllEpisodes(Pageable pageable);
    Episode getEpisodeById(Long id);
    Episode createEpisode(EpisodeCreateDTO dto); // Thay đổi để nhận DTO
    Episode updateEpisode(Long id, Episode episode);
    void deleteEpisode(Long id);
}