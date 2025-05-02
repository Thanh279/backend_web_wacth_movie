package com.test.demo.reponsitory;

import com.test.demo.entity.Series;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeriesRepository extends JpaRepository<Series, Long> {
    Optional<Series> findByTmdbId(Long tmdbId);

    @Query("SELECT s FROM Series s WHERE LOWER(s.title) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Series> findByTitleContainingIgnoreCase(String query);
}