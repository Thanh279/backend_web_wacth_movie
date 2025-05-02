package com.test.demo.reponsitory;

import com.test.demo.entity.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserImageRepository extends JpaRepository<UserImage, Long> {
    Optional<UserImage> findByUserId(Long userId);
}