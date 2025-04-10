package com.test.demo.service.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.demo.dto.AnimationCreateDTO;
import com.test.demo.entity.Animation;
import com.test.demo.entity.Director;
import com.test.demo.entity.Episode;
import com.test.demo.entity.Genre;
import com.test.demo.entity.Studio;
import com.test.demo.reponsitory.AnimationRepository;
import com.test.demo.reponsitory.DirectorRepository;
import com.test.demo.reponsitory.EpisodeRepository;
import com.test.demo.reponsitory.GenreRepository;
import com.test.demo.reponsitory.StudioRepository;
import com.test.demo.service.AnimationService;
@Service
public class AnimationServiceImpl implements AnimationService {
    @Autowired
    private AnimationRepository animationRepository;
    @Autowired
    private DirectorRepository directorRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private StudioRepository studioRepository;
    @Autowired
    private EpisodeRepository episodeRepository;

    @Override
public Animation createAnimation(AnimationCreateDTO dto) {
    Director director = directorRepository.findById(dto.getDirectorId())
            .orElseThrow(() -> new RuntimeException("Director not found"));
    List<Genre> genres = genreRepository.findAllById(dto.getGenreId());
    if (genres.size() != dto.getGenreId().size()) {
        throw new RuntimeException("One or more genres not found");
    }
    Studio studio = studioRepository.findById(dto.getStudioId())
            .orElseThrow(() -> new RuntimeException("Studio not found"));

    Animation animation = new Animation();
    animation.setTitle(dto.getTitle());
    animation.setReleaseYear(dto.getReleaseYear());
    animation.setDescription(dto.getDescription());
    animation.setDirector(director);
    animation.setGenres(genres); 
    animation.setStudio(studio);
    animation.setVideoUrl(dto.getVideoUrl());
    animation.setType(dto.getType());
    animation.setImgUrl(dto.getImgUrl());

    return animationRepository.save(animation);
}
    @Override
    public List<Animation> getAllAnimations() {
        return animationRepository.findAll();
    }

    @Override
    public Optional<Animation> getAnimationById(Long id) {
        return animationRepository.findById(id);
    }

    @Override
    public List<Episode> getEpisodesByAnimationId(Long animationId) {
        return episodeRepository.findByAnimationId(animationId);
    }
}