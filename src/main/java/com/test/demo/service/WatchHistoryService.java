package com.test.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.demo.dto.WatchHistoryDTO;
import com.test.demo.dto.request.WatchHistoryRequest;
import com.test.demo.entity.WatchHistoryEntity;
import com.test.demo.reponsitory.WatchHistoryRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WatchHistoryService {

    @Autowired
    private WatchHistoryRepository repository;

    public List<WatchHistoryDTO> getWatchHistory(String username) {
        return repository.findByUsername(username).stream()
                .map(entity -> new WatchHistoryDTO(
                        entity.getId(),
                        entity.getSeriesId(),
                        entity.getTitle(),
                        entity.getSeasonNumber(),
                        entity.getEpisodeNumber(),
                        entity.getPosterPath(),
                        entity.getWatchedAt()))
                .collect(Collectors.toList());
    }

    public void addToWatchHistory(String username, WatchHistoryRequest request) {
     
        WatchHistoryEntity entity = repository
                .findByUsernameAndSeriesIdAndSeasonNumberAndEpisodeNumber(
                        username, request.getSeriesId(), request.getSeasonNumber(), request.getEpisodeNumber())
                .orElseGet(() -> {
                    WatchHistoryEntity newEntity = new WatchHistoryEntity();
                    newEntity.setUsername(username);
                    newEntity.setSeriesId(request.getSeriesId());
                    newEntity.setTitle(request.getTitle());
                    newEntity.setSeasonNumber(request.getSeasonNumber());
                    newEntity.setEpisodeNumber(request.getEpisodeNumber());
                    newEntity.setPosterPath(request.getPosterPath());
                    return newEntity;
                });

        entity.setWatchedAt(LocalDateTime.now());
        repository.save(entity);
    }
}