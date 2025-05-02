package com.test.demo.reponsitory;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.test.demo.entity.Genre;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    boolean existsByNameIgnoreCase(String name);
    Optional<Genre> findByNameIgnoreCase(String name);
    Optional<Genre> findByNormalizedName(String normalizedName);
    Optional<Genre> findByTmdbGenreId(Integer tmdbGenreId);
}