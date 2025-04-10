package com.test.demo.service.serviceimpl;

import com.test.demo.dto.CreateDirectorDTO;
import com.test.demo.entity.Animation;
import com.test.demo.entity.Director;
import com.test.demo.reponsitory.AnimationRepository;
import com.test.demo.reponsitory.DirectorRepository;
import com.test.demo.service.DirectorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DirectorServiceImpl implements DirectorService {
    @Autowired
    private DirectorRepository directorRepository;

    @Autowired
    private AnimationRepository animationRepository;

    @Override
    public Director createDirector(CreateDirectorDTO dto) {
        Director director = new Director();
        director.setName(dto.getName());
        director.setBio(dto.getBio());
        director.setBirthdate(dto.getBirthdate());
        return directorRepository.save(director);
    }

    @Override
    public List<Director> getAllDirectors() {
        return directorRepository.findAll();
    }

    @Override
    public Optional<Director> getDirectorById(Long id) {
        return directorRepository.findById(id);
    }

    @Override
    public Director updateDirector(Long id, CreateDirectorDTO dto) {
        Director director = directorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Director not found with id: " + id));
        director.setName(dto.getName());
        director.setBio(dto.getBio());
        director.setBirthdate(dto.getBirthdate());
        return directorRepository.save(director);
    }

    @Override
    public void deleteDirector(Long id) {
        Director director = directorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Director not found with id: " + id));
        
        List<Animation> animations = animationRepository.findAll();
        boolean isUsed = animations.stream().anyMatch(a -> a.getDirector().getId().equals(id));
        if (isUsed) {
            throw new RuntimeException("Cannot delete director with id: " + id + " because it is referenced by animations.");
        }

        directorRepository.deleteById(id);
    }
}