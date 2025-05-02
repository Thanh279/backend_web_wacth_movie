package com.test.demo.service.serviceimpl;

import com.test.demo.dto.CreateSeriesDTO;
import com.test.demo.dto.EpisodeDTO;
import com.test.demo.dto.SeriesItemDTO;
import com.test.demo.dto.response.SeriesResponseDTO;
import com.test.demo.entity.Episode;
import com.test.demo.entity.Series;
import com.test.demo.reponsitory.EpisodeRepository;
import com.test.demo.reponsitory.SeriesRepository;
import com.test.demo.service.SeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SeriesServiceImpl implements SeriesService {

    @Autowired
    private SeriesRepository seriesRepository;
    @Autowired
    private EpisodeRepository episodeRepository;

    @Override
    public Series createSeries(CreateSeriesDTO dto) {
        if (dto == null || dto.getTmdbId() == null || dto.getTitle() == null || dto.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Dữ liệu series không hợp lệ");
        }

        // Kiểm tra trùng lặp dựa trên tmdbId
        Optional<Series> existingSeries = seriesRepository.findByTmdbId(dto.getTmdbId());
        if (existingSeries.isPresent()) {
            System.out.println("Series đã tồn tại: " + dto.getTitle());
            return existingSeries.get();
        }

        Series series = new Series();
        series.setTmdbId(dto.getTmdbId());
        series.setTitle(dto.getTitle());
        series.setVoteAverage(dto.getVoteAverage());
        series.setFirstAirDate(dto.getFirstAirDate());
        series.setPosterPath(dto.getPosterPath());
        series.setGenreIds(dto.getGenreIds());
        System.out.println("Lưu series: " + dto.getTitle());
        return seriesRepository.save(series);
    }

    @Override
    public List<EpisodeDTO> getEpisodes(Long seriesId) {
        List<EpisodeDTO> episodes = new ArrayList<>();

        if (seriesId == 219246) {
            for (int i = 1; i <= 16; i++) {
                episodes.add(new EpisodeDTO(i, null));
            }
        }
        return episodes;
    }

    @Override
    public List<Series> getAllSeries() {
        List<Series> series = seriesRepository.findAll();
        return series != null ? series : List.of();
    }

    @Override
    public Optional<Series> getSeriesById(Long id) {
        return seriesRepository.findById(id);
    }

    @Override
    public Series updateSeries(Long id, CreateSeriesDTO dto) {
        Series series = seriesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Series not found with id: " + id));
        series.setTitle(dto.getTitle());
        series.setVoteAverage(dto.getVoteAverage());
        series.setFirstAirDate(dto.getFirstAirDate());
        series.setPosterPath(dto.getPosterPath());
        series.setGenreIds(dto.getGenreIds());
        return seriesRepository.save(series);
    }

    @Override
    public void deleteSeries(Long id) {
        if (!seriesRepository.existsById(id)) {
            throw new RuntimeException("Series not found with id: " + id);
        }
        seriesRepository.deleteById(id);
    }

    @Override
    public SeriesResponseDTO getSeriesWithPagination(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Series> seriesPage = seriesRepository.findAll(pageable);

        List<SeriesItemDTO> seriesItems = seriesPage.getContent().stream().map(series -> {
            SeriesItemDTO item = new SeriesItemDTO();
            item.setTmdbId(series.getTmdbId());
            item.setTitle(series.getTitle());
            item.setVoteAverage(series.getVoteAverage());
            item.setFirstAirDate(series.getFirstAirDate());
            item.setPosterPath(series.getPosterPath());
            item.setGenreIds(series.getGenreIds());
            return item;
        }).collect(Collectors.toList());

        SeriesResponseDTO response = new SeriesResponseDTO();
        response.setPage(page);
        response.setResults(seriesItems);
        response.setTotalPages(seriesPage.getTotalPages());
        response.setTotalResults((int) seriesPage.getTotalElements());

        return response;
    }

    @Override
    public List<EpisodeDTO> getEpisodesBySeriesId(Long seriesId) {
        List<Episode> episodes = episodeRepository.findByAnimationId(seriesId);
        return episodes.stream()
                .map(episode -> new EpisodeDTO(
                        episode.getEpisodeNumber(),
                        episode.getVideoUrl()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<Series> searchSeriesByTitle(String query) {
        if (query == null || query.trim().isEmpty()) {
            return List.of();
        }
        return seriesRepository.findByTitleContainingIgnoreCase(query);
    }
}