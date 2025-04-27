package com.test.demo.controller;

import com.test.demo.dto.CreateSeriesDTO;
import com.test.demo.dto.EpisodeDTO;
import com.test.demo.dto.response.SeriesResponseDTO;
import com.test.demo.entity.Series;
import com.test.demo.service.SeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/series")
public class SeriesController {

    @Autowired
    private SeriesService seriesService;

    @PostMapping("/bulk")
    public ResponseEntity<String> createSeriesBulk(@RequestBody List<CreateSeriesDTO> seriesDTOs) {
        try {
            System.out.println("Nhận được " + seriesDTOs.size() + " series từ frontend");
            for (CreateSeriesDTO dto : seriesDTOs) {
                System.out.println("Xử lý series: " + dto.getTitle());
                seriesService.createSeries(dto);
            }
            return ResponseEntity.ok("Series đã được lưu thành công");
        } catch (Exception e) {
            System.out.println("Lỗi khi lưu series: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Lỗi khi lưu series: " + e.getMessage());
        }
    }

    @GetMapping("/{seriesId}/episodes")
    public ResponseEntity<List<EpisodeDTO>> getEpisodesBySeriesId(@PathVariable Long seriesId) {
        List<EpisodeDTO> episodes = seriesService.getEpisodesBySeriesId(seriesId);
        return ResponseEntity.ok(episodes);
    }

    @GetMapping
    public ResponseEntity<List<Series>> getAllSeries() {
        return ResponseEntity.ok(seriesService.getAllSeries());
    }

    @GetMapping("/content")
    public ResponseEntity<SeriesResponseDTO> getContent(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize) {
        SeriesResponseDTO response = seriesService.getSeriesWithPagination(page, pageSize);
        return ResponseEntity.ok(response);
    }
}