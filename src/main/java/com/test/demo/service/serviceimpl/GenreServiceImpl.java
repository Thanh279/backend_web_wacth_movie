package com.test.demo.service.serviceimpl;

   import com.test.demo.dto.CreateGenreDTO;
   import com.test.demo.entity.Genre;
   import com.test.demo.reponsitory.GenreRepository;
   import com.test.demo.service.GenreService;
   import org.springframework.beans.factory.annotation.Autowired;
   import org.springframework.stereotype.Service;

   import java.text.Normalizer;
   import java.util.Collections;
   import java.util.List;
   import java.util.Optional;
   import java.util.regex.Pattern;

   @Service
   public class GenreServiceImpl implements GenreService {

       @Autowired
       private GenreRepository genreRepository;

       @Override
       public Genre createGenre(CreateGenreDTO dto) {
           if (dto == null || dto.getName() == null || dto.getName().trim().isEmpty()) {
               throw new IllegalArgumentException("Tên thể loại không được để trống");
           }

           String normalizedName = normalizeName(dto.getName());
           Optional<Genre> existingGenre = genreRepository.findByNormalizedName(normalizedName);
           if (existingGenre.isPresent()) {
               System.out.println("Thể loại đã tồn tại: " + normalizedName);
               Genre genre = existingGenre.get();
               if (dto.getTmdbGenreId() != null && !dto.getTmdbGenreId().equals(genre.getTmdbGenreId())) {
                   genre.setTmdbGenreId(dto.getTmdbGenreId());
                   System.out.println("Cập nhật tmdbGenreId cho " + dto.getName() + ": " + dto.getTmdbGenreId());
                   return genreRepository.save(genre);
               }
               return genre;
           }

           Genre genre = new Genre();
           genre.setName(dto.getName());
           genre.setNormalizedName(normalizedName);
           genre.setTmdbGenreId(dto.getTmdbGenreId());
           System.out.println("Lưu thể loại: " + dto.getName() + ", tmdbGenreId: " + dto.getTmdbGenreId());
           return genreRepository.save(genre);
       }

       @Override
       public List<Genre> getAllGenres() {
           List<Genre> genres = genreRepository.findAll();
           System.out.println("Genres retrieved: " + genres);
           return genres != null ? genres : Collections.emptyList();
       }

       @Override
       public Optional<Genre> getGenreById(Long id) {
           return genreRepository.findById(id);
       }

       @Override
       public Optional<Genre> getGenreByTmdbGenreId(Integer tmdbGenreId) {
           return genreRepository.findByTmdbGenreId(tmdbGenreId);
       }

       @Override
       public Genre updateGenre(Long id, CreateGenreDTO dto) {
           Genre genre = genreRepository.findById(id)
                   .orElseThrow(() -> new RuntimeException("Genre not found with id: " + id));
           genre.setName(dto.getName());
           genre.setNormalizedName(normalizeName(dto.getName()));
           genre.setTmdbGenreId(dto.getTmdbGenreId());
           return genreRepository.save(genre);
       }

       @Override
       public void deleteGenre(Long id) {
           if (!genreRepository.existsById(id)) {
               throw new RuntimeException("Genre not found with id: " + id);
           }
           genreRepository.deleteById(id);
       }

       private String normalizeName(String name) {
           String normalized = name.replaceFirst("(?i)^Phim\\s*", "").trim();
           String temp = Normalizer.normalize(normalized, Normalizer.Form.NFD);
           Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
           normalized = pattern.matcher(temp).replaceAll("").toLowerCase();
           return normalized;
       }
   }