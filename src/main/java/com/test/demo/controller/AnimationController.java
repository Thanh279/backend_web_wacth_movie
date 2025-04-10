package com.test.demo.controller;

import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import java.io.File;
import org.springframework.core.io.FileSystemResource;


import org.springframework.core.io.Resource;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.test.demo.dto.AnimationCreateDTO;
import com.test.demo.entity.Animation;
import com.test.demo.entity.Episode;
import com.test.demo.service.AnimationService;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/animations")
@CrossOrigin(origins = "http://localhost:5173/")
public class AnimationController {
    @Autowired
    private AnimationService animationService;

    @PostMapping
    public ResponseEntity<Animation> createAnimation(@RequestBody AnimationCreateDTO dto) {
        Animation savedAnimation = animationService.createAnimation(dto);
        return new ResponseEntity<>(savedAnimation, HttpStatus.CREATED);
    }

    @GetMapping(value = "/videos/{filename}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> getVideo(@PathVariable String filename) {
        File file = new File("/path/to/videos/" + filename);
        System.out.println("File path: " + file.getAbsolutePath());
    
        if (!file.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    
        Resource resource = new FileSystemResource(file);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
    

    @GetMapping
    public ResponseEntity<List<Animation>> getAllAnimations() {
        List<Animation> animations = animationService.getAllAnimations();
        return ResponseEntity.ok(animations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Animation> getAnimationById(@PathVariable Long id) {
        Optional<Animation> animation = animationService.getAnimationById(id);
        return animation.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/{id}/episodes")
    public ResponseEntity<List<Episode>> getEpisodesByAnimationId(@PathVariable Long id) {
        List<Episode> episodes = animationService.getEpisodesByAnimationId(id);
        return ResponseEntity.ok(episodes);
    }
}