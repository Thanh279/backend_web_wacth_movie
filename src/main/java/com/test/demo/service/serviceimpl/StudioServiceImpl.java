package com.test.demo.service.serviceimpl;
import com.test.demo.dto.CreateStudioDTO;
import com.test.demo.entity.Animation;
import com.test.demo.entity.Studio;
import com.test.demo.reponsitory.AnimationRepository;
import com.test.demo.reponsitory.StudioRepository;
import com.test.demo.service.StudioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudioServiceImpl implements StudioService {
    @Autowired
    private StudioRepository studioRepository;

    @Autowired
    private AnimationRepository animationRepository;

    @Override
    public Studio createStudio(CreateStudioDTO dto) {
        Studio studio = new Studio();
        studio.setName(dto.getName());
        studio.setAddress(dto.getAddress());
        studio.setWebsite(dto.getWebsite());
        return studioRepository.save(studio);
    }

    @Override
    public List<Studio> getAllStudios() {
        return studioRepository.findAll();
    }

    @Override
    public Optional<Studio> getStudioById(Long id) {
        return studioRepository.findById(id);
    }

    @Override
    public Studio updateStudio(Long id, CreateStudioDTO dto) {
        Studio studio = studioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Studio not found with id: " + id));
        studio.setName(dto.getName());
        studio.setAddress(dto.getAddress());
        studio.setWebsite(dto.getWebsite());
        return studioRepository.save(studio);
    }

    @Override
    public void deleteStudio(Long id) {
        Studio studio = studioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Studio not found with id: " + id));
        
        List<Animation> animations = animationRepository.findAll();
        boolean isUsed = animations.stream().anyMatch(a -> a.getStudio().getId().equals(id));
        if (isUsed) {
            throw new RuntimeException("Cannot delete studio with id: " + id + " because it is referenced by animations.");
        }

        studioRepository.deleteById(id);
    }
}