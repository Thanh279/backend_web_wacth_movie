package com.test.demo.service;

import java.util.List;
import java.util.Optional;

import com.test.demo.dto.CreateStudioDTO;
import com.test.demo.entity.Studio;

public interface StudioService {
    Studio createStudio(CreateStudioDTO dto);
    List<Studio> getAllStudios();
    Optional<Studio> getStudioById(Long id);
    Studio updateStudio(Long id, CreateStudioDTO dto);
    void deleteStudio(Long id);
}