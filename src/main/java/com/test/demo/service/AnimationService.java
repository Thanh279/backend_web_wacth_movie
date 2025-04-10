package com.test.demo.service;

import java.util.List;
import java.util.Optional;



import com.test.demo.dto.AnimationCreateDTO;
import com.test.demo.entity.Animation;
import com.test.demo.entity.Episode;

public interface AnimationService {
    Animation createAnimation(AnimationCreateDTO dto);
  
    List<Animation> getAllAnimations();
    Optional<Animation> getAnimationById(Long id);
    List<Episode> getEpisodesByAnimationId(Long animationId);
}