package com.test.demo.reponsitory;

import com.test.demo.entity.FavoriteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<FavoriteEntity, String> {
    List<FavoriteEntity> findByUsername(String username);
    Optional<FavoriteEntity> findByUsernameAndSeriesId(String username, String seriesId);
}