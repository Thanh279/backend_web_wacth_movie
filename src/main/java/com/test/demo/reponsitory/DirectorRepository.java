package com.test.demo.reponsitory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.test.demo.entity.Director;

public interface DirectorRepository extends JpaRepository<Director, Long> {
}