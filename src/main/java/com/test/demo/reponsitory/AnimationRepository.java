package com.test.demo.reponsitory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.test.demo.entity.Animation;

public interface AnimationRepository extends JpaRepository<Animation, Long> {
}