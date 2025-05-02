package com.test.demo.service;

import com.test.demo.entity.Comment;
import java.util.List;

public interface CommentService {
    List<Comment> saveComments(Long seriesId, List<Comment> comments);
    List<Comment> getCommentsBySeriesId(Long seriesId);
    Comment saveComment(Comment comment);
}