package com.test.demo.reponsitory;

import com.test.demo.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findBySeriesId(Long seriesId);
}