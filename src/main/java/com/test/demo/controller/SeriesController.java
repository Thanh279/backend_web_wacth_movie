package com.test.demo.controller;

import com.test.demo.dto.CreateSeriesDTO;
import com.test.demo.dto.response.SeriesResponseDTO;
import com.test.demo.entity.Comment;
import com.test.demo.entity.Series;
import com.test.demo.service.CommentService;
import com.test.demo.service.SeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/series")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class SeriesController {

    @Autowired
    private SeriesService seriesService;

    @Autowired
    private CommentService commentService;

    @GetMapping
    public ResponseEntity<List<Series>> getAllSeries() {
        List<Series> series = seriesService.getAllSeries();
        return ResponseEntity.ok(series);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Series> getSeriesById(@PathVariable Long id) {
        Optional<Series> series = seriesService.getSeriesById(id);
        return series.map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Series> createSeries(@RequestBody CreateSeriesDTO seriesDTO) {
        Series createdSeries = seriesService.createSeries(seriesDTO);
        return new ResponseEntity<>(createdSeries, HttpStatus.CREATED);
    }

    @PostMapping("/bulk")
    public ResponseEntity<String> bulkCreateSeries(@RequestBody List<CreateSeriesDTO> seriesDTOs) {
        for (CreateSeriesDTO dto : seriesDTOs) {
            seriesService.createSeries(dto);
        }
        return ResponseEntity.ok("Series đã được lưu thành công");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Series> updateSeries(@PathVariable Long id, @RequestBody CreateSeriesDTO seriesDTO) {
        Series updatedSeries = seriesService.updateSeries(id, seriesDTO);
        return ResponseEntity.ok(updatedSeries);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeries(@PathVariable Long id) {
        seriesService.deleteSeries(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/content")
    public ResponseEntity<SeriesResponseDTO> getSeriesWithPagination(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        SeriesResponseDTO response = seriesService.getSeriesWithPagination(page, pageSize);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{seriesId}/episodes")
    public ResponseEntity<List<com.test.demo.dto.EpisodeDTO>> getEpisodesBySeriesId(@PathVariable Long seriesId) {
        List<com.test.demo.dto.EpisodeDTO> episodes = seriesService.getEpisodesBySeriesId(seriesId);
        return ResponseEntity.ok(episodes);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Series>> searchSeries(@RequestParam String query) {
        List<Series> series = seriesService.searchSeriesByTitle(query);
        return ResponseEntity.ok(series);
    }

    @PostMapping("/{seriesId}/comments/bulk")
    public ResponseEntity<String> bulkSaveComments(@PathVariable Long seriesId, @RequestBody List<Comment> comments) {
        commentService.saveComments(seriesId, comments);
        return ResponseEntity.ok("Comments đã được lưu thành công");
    }

    @GetMapping("/{seriesId}/comments")
    public ResponseEntity<List<Comment>> getCommentsBySeriesId(@PathVariable Long seriesId) {
        List<Comment> comments = commentService.getCommentsBySeriesId(seriesId);
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/{seriesId}/comments")
    public ResponseEntity<Comment> addComment(@PathVariable Long seriesId, @RequestBody Comment comment) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String username = authentication.getName();
        comment.setSeriesId(seriesId);
        comment.setAuthor(username);
        comment.setCreatedAt(LocalDateTime.now());
        Comment savedComment = commentService.saveComment(comment);
        return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
    }
}