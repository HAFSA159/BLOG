package com.blogapp.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.apache.logging.log4j.Logger;

import com.blogapp.config.LoggerConfig;
import com.blogapp.model.Comment;
import com.blogapp.model.CommentStatus;
import com.blogapp.repository.CommentRepository;

public class CommentRepositoryImpl implements CommentRepository {

    private static final Logger logger = LoggerConfig.getLogger(CommentRepositoryImpl.class);
    private final EntityManager entityManager;

    public CommentRepositoryImpl() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("blogapp");
        this.entityManager = emf.createEntityManager();
    }

    @Override
    public List<Comment> findAll() {
        logger.debug("Finding all comments");
        String jpql = "SELECT c FROM Comment c";
        return entityManager.createQuery(jpql, Comment.class).getResultList();
    }

    @Override
    public List<Comment> findByAuthorEmail(String email) {
        logger.debug("Finding comments by author email: {}", email);
        String jpql = "SELECT c FROM Comment c WHERE c.author.email = :email";
        return entityManager.createQuery(jpql, Comment.class)
                .setParameter("email", email)
                .getResultList();
    }

    @Override
    public List<Comment> findAllByArticleId(Long articleId) {
        logger.debug("Finding comments for article ID: {}", articleId);
        String jpql = "FROM Comment c WHERE c.article.id = :articleId ORDER BY c.creationDate DESC";
        List<Comment> comments = entityManager.createQuery(jpql, Comment.class)
                .setParameter("articleId", articleId)
                .getResultList();
        logger.debug("Query executed. Number of comments found: {}", comments.size());
        return comments;
    }

    @Override
    public Comment findById(Long id) {
        logger.debug("Finding comment by ID: {}", id);
        return entityManager.find(Comment.class, id);
    }

    @Override
    public void save(Comment comment) {
        logger.info("Saving new comment for article ID: {}", comment.getArticle().getId());
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(comment);
            transaction.commit();
            logger.info("Comment saved successfully. ID: {}", comment.getId());
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            logger.error("Error saving comment: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void update(Comment comment) {
        logger.info("Updating comment ID: {}", comment.getId());
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(comment);
            transaction.commit();
            logger.info("Comment updated successfully. ID: {}", comment.getId());
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            logger.error("Error updating comment: {}", e.getMessage(), e);
            throw e;
        }
    }
@Override
public List<Comment> findApprovedByArticleId(Long articleId) {
    String jpql = "SELECT c FROM Comment c WHERE c.article.id = :articleId AND c.status = :status ORDER BY c.creationDate DESC";
    return entityManager.createQuery(jpql, Comment.class)
            .setParameter("articleId", articleId)
            .setParameter("status", CommentStatus.approved)
            .getResultList();
}
    @Override
    public void delete(Long id) {
        logger.info("Deleting comment ID: {}", id);
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Comment comment = entityManager.find(Comment.class, id);
            if (comment != null) {
                entityManager.remove(comment);
                logger.info("Comment deleted successfully. ID: {}", id);
            } else {
                logger.warn("Comment not found for deletion. ID: {}", id);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            logger.error("Error deleting comment: {}", e.getMessage(), e);
            throw e;
        }
    }
}
