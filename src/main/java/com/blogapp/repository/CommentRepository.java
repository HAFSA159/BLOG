package com.blogapp.repository;

import java.util.List;

import com.blogapp.model.Comment;

public interface CommentRepository {
    List<Comment> findAllByArticleId(Long articleId, int offset, int limit);
    Comment findById(Long id);
    void save(Comment comment);
    void update(Comment comment);
    void delete(Long id);
    int getNoOfRecords(Long articleId);
    List<Comment> findAll();
}
