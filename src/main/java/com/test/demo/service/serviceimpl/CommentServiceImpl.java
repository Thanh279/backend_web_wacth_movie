package com.test.demo.service.serviceimpl;

import com.test.demo.entity.Comment;
import com.test.demo.reponsitory.CommentRepository;
import com.test.demo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comment> saveComments(Long seriesId, List<Comment> comments) {
        comments.forEach(comment -> {
            comment.setSeriesId(seriesId);
            comment.setCreatedAt(LocalDateTime.now()); // Override with current time for simplicity
        });
        return commentRepository.saveAll(comments);
    }

    @Override
    public List<Comment> getCommentsBySeriesId(Long seriesId) {
        return commentRepository.findBySeriesId(seriesId);
    }

    @Override
    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }
}