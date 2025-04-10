package com.test.demo.reponsitory;
import org.springframework.data.jpa.repository.JpaRepository;

import com.test.demo.entity.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}