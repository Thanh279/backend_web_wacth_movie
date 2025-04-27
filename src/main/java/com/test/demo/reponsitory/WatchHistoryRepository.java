package com.test.demo.reponsitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.test.demo.entity.WatchHistoryEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface WatchHistoryRepository extends JpaRepository<WatchHistoryEntity, String> {
    List<WatchHistoryEntity> findByUsername(String username);

    Optional<WatchHistoryEntity> findByUsernameAndSeriesIdAndSeasonNumberAndEpisodeNumber(
            String username, String seriesId, Integer seasonNumber, Integer episodeNumber);
}