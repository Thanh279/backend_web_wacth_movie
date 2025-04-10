package com.test.demo.service;

import java.util.List;
import java.util.Optional;

import com.test.demo.dto.CreateGenreDTO;
import com.test.demo.entity.Genre;

public interface GenreService {
    Genre createGenre(CreateGenreDTO dto);
    List<Genre> getAllGenres();
    Optional<Genre> getGenreById(Long id);
    Genre updateGenre(Long id, CreateGenreDTO dto);
    void deleteGenre(Long id);
}