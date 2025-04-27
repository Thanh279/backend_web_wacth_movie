package com.test.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.test.demo.dto.WatchHistoryDTO;
import com.test.demo.dto.request.WatchHistoryRequest;
import com.test.demo.service.WatchHistoryService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class WatchHistoryController {

    @Autowired
    private WatchHistoryService watchHistoryService;

    @GetMapping("/watch-history")
    public ResponseEntity<?> getWatchHistory(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }
        String username = authentication.getName();
        List<WatchHistoryDTO> history = watchHistoryService.getWatchHistory(username);
        return ResponseEntity.ok(Map.of("data", history));
    }

    @PostMapping("/watch-history")
    public ResponseEntity<Map<String, String>> addToWatchHistory(
            @RequestBody WatchHistoryRequest request,
            Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }
        String username = authentication.getName();
        watchHistoryService.addToWatchHistory(username, request);
        return ResponseEntity.ok(Map.of("message", "Watch history updated"));
    }
}