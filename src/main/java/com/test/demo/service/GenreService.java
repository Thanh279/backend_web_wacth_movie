package com.test.demo.service;

   import com.test.demo.dto.CreateGenreDTO;
   import com.test.demo.entity.Genre;

   import java.util.List;
   import java.util.Optional;

   public interface GenreService {
       Genre createGenre(CreateGenreDTO dto);
       List<Genre> getAllGenres();
       Optional<Genre> getGenreById(Long id);
       Optional<Genre> getGenreByTmdbGenreId(Integer tmdbGenreId);
       Genre updateGenre(Long id, CreateGenreDTO dto);
       void deleteGenre(Long id);
   }