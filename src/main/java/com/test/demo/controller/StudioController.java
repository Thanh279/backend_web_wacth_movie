package com.test.demo.controller;

import com.test.demo.dto.CreateStudioDTO;
import com.test.demo.entity.Studio;
import com.test.demo.service.StudioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/studios")
public class StudioController {
    @Autowired
    private StudioService studioService;

    @PostMapping
    public ResponseEntity<Studio> createStudio(@RequestBody CreateStudioDTO dto) {
        Studio createdStudio = studioService.createStudio(dto);
        return new ResponseEntity<>(createdStudio, HttpStatus.CREATED);
    }

    @GetMapping
    public List<Studio> getAllStudios() {
        return studioService.getAllStudios();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Studio> getStudioById(@PathVariable Long id) {
        Optional<Studio> studio = studioService.getStudioById(id);
        return studio.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Studio> updateStudio(@PathVariable Long id, @RequestBody CreateStudioDTO dto) {
        Studio updatedStudio = studioService.updateStudio(id, dto);
        return ResponseEntity.ok(updatedStudio);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudio(@PathVariable Long id) {
        try {
            studioService.deleteStudio(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }
}