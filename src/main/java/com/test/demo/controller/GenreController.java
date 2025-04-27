package com.test.demo.controller;

import com.test.demo.dto.CreateGenreDTO;
import com.test.demo.entity.Genre;
import com.test.demo.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
                System.out.println("Xử lý thể loại: " + dto.getName());
                if (dto.getName() == null || dto.getName().trim().isEmpty()) {
                    throw new IllegalArgumentException("Tên thể loại không được để trống");
                }
                genreService.createGenre(dto);
            }
            return ResponseEntity.ok("Thể loại đã được lưu thành công");
        } catch (Exception e) {
            System.out.println("Lỗi khi lưu thể loại: " + e.getMessage());
            e.printStackTrace(); // In chi tiết lỗi ra console
            return ResponseEntity.badRequest().body("Lỗi khi lưu thể loại: " + e.getMessage());
        }
    }

    // Các endpoint khác nếu cần
    @GetMapping
    public ResponseEntity<List<Genre>> getAllGenres() {
        return ResponseEntity.ok(genreService.getAllGenres());
    }
}