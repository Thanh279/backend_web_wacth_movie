package com.test.demo.reponsitory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.test.demo.entity.Studio;

public interface StudioRepository extends JpaRepository<Studio, Long> {
}