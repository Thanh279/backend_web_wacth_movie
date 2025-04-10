
package com.test.demo.service;

import com.test.demo.dto.CreateDirectorDTO;
import com.test.demo.entity.Director;

import java.util.List;
import java.util.Optional;

public interface DirectorService {
    Director createDirector(CreateDirectorDTO dto);
    List<Director> getAllDirectors();
    Optional<Director> getDirectorById(Long id);
    Director updateDirector(Long id, CreateDirectorDTO dto);
    void deleteDirector(Long id);
}