package com.test.demo.controller;

import com.test.demo.dto.CreateGenreDTO;
import com.test.demo.entity.Genre;
import com.test.demo.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/genres")
public class GenreController {

    @Autowired
    private GenreService genreService;

    @PostMapping("/bulk")
    public ResponseEntity<String> createGenresBulk(@RequestBody List<CreateGenreDTO> genreDTOs) {
        try {
            System.out.println("Nhận được " + genreDTOs.size() + " thể loại từ frontend");
            for (CreateGenreDTO dto : genreDTOs) {
                System.out.println("Xử lý thể loại: " + dto.getName() + ", tmdbGenreId: " + dto.getTmdbGenreId());
                if (dto.getName() == null || dto.getName().trim().isEmpty()) {
                    throw new IllegalArgumentException("Tên thể loại không được để trống");
                }
                genreService.createGenre(dto);
            }
            return ResponseEntity.ok("Thể loại đã được lưu thành công");
        } catch (Exception e) {
            System.out.println("Lỗi khi lưu thể loại: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Lỗi khi lưu thể loại: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllGenres() {
        try {
            List<Genre> genres = genreService.getAllGenres();
            System.out.println("Genres fetched: " + genres);
            Map<String, Object> response = new HashMap<>();
            response.put("data", genres);
            response.put("statusCode", 200);
            response.put("message", "CALL API SUCCESS");
            System.out.println("Response: " + response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy danh sách thể loại: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("statusCode", 500);
            response.put("message", "Lỗi server: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getGenreById(@PathVariable Long id) {
        try {
            Optional<Genre> genre = genreService.getGenreById(id);
            if (genre.isPresent()) {
                Map<String, Object> genreData = new HashMap<>();
                genreData.put("id", genre.get().getId());
                genreData.put("name", genre.get().getName());
                genreData.put("tmdbGenreId", genre.get().getTmdbGenreId());
                Map<String, Object> response = new HashMap<>();
                response.put("data", genreData);
                response.put("statusCode", 200);
                response.put("message", "CALL API SUCCESS");
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("statusCode", 404);
                response.put("message", "Thể loại không tồn tại với ID: " + id);
                return ResponseEntity.status(404).body(response);
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy thể loại với ID " + id + ": " + e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("statusCode", 400);
            response.put("message", "Lỗi khi lấy thể loại: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/by-tmdb/{tmdbGenreId}")
    public ResponseEntity<Map<String, Object>> getGenreByTmdbGenreId(@PathVariable Integer tmdbGenreId) {
        try {
            Optional<Genre> genre = genreService.getGenreByTmdbGenreId(tmdbGenreId);
            if (genre.isPresent()) {
                Map<String, Object> genreData = new HashMap<>();
                genreData.put("id", genre.get().getId());
                genreData.put("name", genre.get().getName());
                genreData.put("tmdbGenreId", genre.get().getTmdbGenreId());
                Map<String, Object> response = new HashMap<>();
                response.put("data", genreData);
                response.put("statusCode", 200);
                response.put("message", "CALL API SUCCESS");
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("statusCode", 404);
                response.put("message", "Thể loại không tồn tại với TMDB Genre ID: " + tmdbGenreId);
                return ResponseEntity.status(404).body(response);
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy thể loại với TMDB Genre ID " + tmdbGenreId + ": " + e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("statusCode", 400);
            response.put("message", "Lỗi khi lấy thể loại: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}