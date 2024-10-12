package com.blogapp.service;

import java.util.List;

import org.apache.logging.log4j.Logger;

import com.blogapp.config.LoggerConfig;
import com.blogapp.model.Comment;
import com.blogapp.model.CommentStatus;
import com.blogapp.repository.CommentRepository;

public class CommentService {
    private static final Logger logger = LoggerConfig.getLogger(CommentService.class);
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> getAllCommentsByArticleId(Long articleId) {
        logger.info("Getting all comments for article ID: {}", articleId);
        return commentRepository.findAllByArticleId(articleId);
    }

    public Comment getCommentById(Long id) {
        logger.info("Getting comment by ID: {}", id);
        return commentRepository.findById(id);
    }

    public void addComment(Comment comment) {
        logger.info("Adding new comment for article ID: {}", comment.getArticle().getId());
        commentRepository.save(comment);
    }

    public void updateComment(Comment comment) {
        logger.info("Updating comment ID: {}", comment.getId());
        commentRepository.update(comment);
    }

    public void deleteComment(Long id) {
        logger.info("Deleting comment ID: {}", id);
        commentRepository.delete(id);
    }

    public void updateCommentStatus(Long id, CommentStatus status) {
        logger.info("Updating status of comment ID: {} to {}", id, status);
        Comment comment = commentRepository.findById(id);
        if (comment != null) {
            comment.setStatus(status);
            commentRepository.update(comment);
        } else {
            throw new IllegalArgumentException("Comment not found with id: " + id);
        }
    }
    public List<Comment> getAllComments() {
        logger.info("Getting all comments");
        return commentRepository.findAll();
    }
    
    public List<Comment> getCommentsByAuthorEmail(String email) {
        logger.info("Getting comments for author email: {}", email);
        return commentRepository.findByAuthorEmail(email);
    }
    public List<Comment> getApprovedCommentsByArticleId(Long articleId) {
        logger.info("Getting approved comments for article ID: {}", articleId);
        return commentRepository.findApprovedByArticleId(articleId);
    }
}