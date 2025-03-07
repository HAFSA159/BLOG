package com.blogapp.repository;

import java.util.List;

import com.blogapp.model.Comment;

public interface CommentRepository {
    List<Comment> findAllByArticleId(Long articleId);
    Comment findById(Long id);
    void save(Comment comment);
    void update(Comment comment);
    void delete(Long id);
    List<Comment> findAll();
    List<Comment> findByAuthorEmail(String email);
    List<Comment> findApprovedByArticleId(Long articleId);
}