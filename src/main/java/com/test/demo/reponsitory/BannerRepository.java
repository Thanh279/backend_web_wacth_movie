package com.test.demo.reponsitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.test.demo.entity.Banner;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {
}