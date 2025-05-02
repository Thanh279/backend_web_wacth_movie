package com.test.demo.service;

import com.test.demo.dto.FavoriteDTO;
import com.test.demo.dto.request.FavoriteRequest;
import com.test.demo.entity.FavoriteEntity;
import com.test.demo.reponsitory.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository repository;

    public List<FavoriteDTO> getFavorites(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Tên người dùng không được để trống.");
        }
        return repository.findByUsername(username).stream()
                .sorted(Comparator.comparing(FavoriteEntity::getAddedAt).reversed()) // Sort by addedAt in descending order
                .map(entity -> new FavoriteDTO(
                        entity.getId(),
                        entity.getSeriesId(),
                        entity.getTitle(),
                        entity.getPosterPath(),
                        entity.getAddedAt()))
                .collect(Collectors.toList());
    }

    public void addToFavorites(String username, FavoriteRequest request) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Tên người dùng không được để trống.");
        }
        if (request.getSeriesId() == null || request.getSeriesId().isEmpty()) {
            throw new IllegalArgumentException("seriesId không được để trống.");
        }
        if (request.getTitle() == null || request.getTitle().isEmpty()) {
            throw new IllegalArgumentException("title không được để trống.");
        }

        FavoriteEntity entity = repository
                .findByUsernameAndSeriesId(username, request.getSeriesId())
                .orElseGet(() -> {
                    FavoriteEntity newEntity = new FavoriteEntity();
                    newEntity.setUsername(username);
                    newEntity.setSeriesId(request.getSeriesId());
                    newEntity.setTitle(request.getTitle());
                    newEntity.setPosterPath(request.getPosterPath());
                    return newEntity;
                });

        entity.setAddedAt(LocalDateTime.now());
        repository.save(entity);
    }

    public void removeFavorite(String username, String favoriteId) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Tên người dùng không được để trống.");
        }
        if (favoriteId == null || favoriteId.isEmpty()) {
            throw new IllegalArgumentException("favoriteId không được để trống.");
        }

        FavoriteEntity entity = repository.findById(favoriteId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy mục yêu thích với ID: " + favoriteId));
        if (!entity.getUsername().equals(username)) {
            throw new IllegalArgumentException("Bạn không có quyền xóa mục yêu thích này.");
        }
        repository.delete(entity);
    }
}