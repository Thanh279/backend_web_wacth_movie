package com.test.demo.controller;

import com.test.demo.dto.FavoriteDTO;
import com.test.demo.dto.request.FavoriteRequest;
import com.test.demo.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class FavoriteController {

    @Autowired
    private FavoriteService service;

    @GetMapping("/favorites")
    public ResponseEntity<?> getFavorites(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }
        String username = authentication.getName();
        List<FavoriteDTO> favorites = service.getFavorites(username);
        return ResponseEntity.ok(Map.of("data", favorites));
    }

    @PostMapping("/favorites")
    public ResponseEntity<Map<String, String>> addToFavorites(
            @RequestBody FavoriteRequest request,
            Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }

        // Kiểm tra dữ liệu đầu vào
        if (request.getSeriesId() == null || request.getSeriesId().isEmpty()) {
            return ResponseEntity.status(400)
                    .body(Map.of("error", "Yêu cầu không hợp lệ: seriesId không được để trống."));
        }
        if (request.getTitle() == null || request.getTitle().isEmpty()) {
            return ResponseEntity.status(400).body(Map.of("error", "Yêu cầu không hợp lệ: title không được để trống."));
        }

        String username = authentication.getName();
        request.setId(UUID.randomUUID().toString()); // Tạo ID thủ công
        service.addToFavorites(username, request);
        return ResponseEntity.ok(Map.of("message", "Favorite updated"));
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity<Map<String, String>> removeFavorite(
            Authentication authentication,
            @PathVariable String id) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        } 

        String username = authentication.getName();
        service.removeFavorite(username, id);
        return ResponseEntity.ok(Map.of("message", "Favorite removed"));
    }
}