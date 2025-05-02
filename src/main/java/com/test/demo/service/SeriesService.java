package com.test.demo.service;

import com.test.demo.dto.CreateSeriesDTO;
import com.test.demo.dto.EpisodeDTO;
import com.test.demo.dto.response.SeriesResponseDTO;
import com.test.demo.entity.Series;

import java.util.List;
import java.util.Optional;

public interface SeriesService {
    Series createSeries(CreateSeriesDTO dto);
    List<EpisodeDTO> getEpisodes(Long seriesId);
    List<Series> getAllSeries();
    Optional<Series> getSeriesById(Long id);
    Series updateSeries(Long id, CreateSeriesDTO dto);
    void deleteSeries(Long id);
    SeriesResponseDTO getSeriesWithPagination(int page, int pageSize);
    List<EpisodeDTO> getEpisodesBySeriesId(Long seriesId);
    List<Series> searchSeriesByTitle(String query); 
}