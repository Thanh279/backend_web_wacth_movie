package com.test.demo.controller;

import com.test.demo.dto.EpisodeCreateDTO;
import com.test.demo.entity.Episode;
import com.test.demo.service.EpisodeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/episodes")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class EpisodeController {

    @Autowired
    private EpisodeService episodeService;

    @Autowired
    private ResourceLoader resourceLoader;

    @PostMapping("/{episodeId}/uploadDash")
    public ResponseEntity<String> uploadDashFiles(
            @PathVariable Long episodeId,
            @RequestParam("mpdFile") MultipartFile mpdFile,
            @RequestParam("m4sFiles") List<MultipartFile> m4sFiles) {

        Episode episode = episodeService.getEpisodeById(episodeId);

        try {

            Resource resource = resourceLoader.getResource("classpath:static/phim/");
            File uploadBaseDir = resource.getFile();
            String uploadDir = uploadBaseDir.getAbsolutePath() + "/" + episodeId + "/";
            Path directoryPath = Paths.get(uploadDir);

            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }

            // Xử lý file .mpd
            String mpdFileName = mpdFile.getOriginalFilename();
            if (mpdFileName == null || !mpdFileName.endsWith(".mpd")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid .mpd file");
            }
            if (mpdFile.getSize() > 1024 * 1024 * 100) { // 100MB
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "MPD file too large");
            }
            Path mpdPath = directoryPath.resolve(mpdFileName);
            mpdFile.transferTo(mpdPath.toFile());

            String mpdUrl = "/phim/" + episodeId + "/" + mpdFileName;
            episode.setVideoUrl(mpdUrl);
            episodeService.updateEpisode(episodeId, episode);

            for (MultipartFile m4sFile : m4sFiles) {
                String m4sFileName = m4sFile.getOriginalFilename();
                if (m4sFileName != null && m4sFileName.endsWith(".m4s")) {
                    if (m4sFile.getSize() > 1024 * 1024 * 100) { // 100MB
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "M4S file too large");
                    }
                    Path m4sPath = directoryPath.resolve(m4sFileName);
                    m4sFile.transferTo(m4sPath.toFile());
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid .m4s file");
                }
            }

            return ResponseEntity.ok("Files uploaded successfully. MPD URL: " + mpdUrl);
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Upload failed: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Episode>> getAllEpisodes() {
        List<Episode> episodes = episodeService.getAllEpisodes();
        return ResponseEntity.ok(episodes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Episode> getEpisodeById(@PathVariable Long id) {
        return new ResponseEntity<>(episodeService.getEpisodeById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Episode> createEpisode(@Valid @RequestBody EpisodeCreateDTO dto) {
        Episode episode = episodeService.createEpisode(dto);
        return new ResponseEntity<>(episode, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Episode> updateEpisode(@PathVariable Long id, @RequestBody Episode episode) {
        Episode updatedEpisode = episodeService.updateEpisode(id, episode);
        return new ResponseEntity<>(updatedEpisode, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEpisode(@PathVariable Long id) {
        episodeService.deleteEpisode(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}