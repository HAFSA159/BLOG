package com.blogapp.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.apache.logging.log4j.Logger;

import com.blogapp.config.LoggerConfig;
import com.blogapp.model.Article;
import com.blogapp.repository.ArticleRepository;
import com.blogapp.util.HibernateUtil;

public class ArticleRepositoryImpl implements ArticleRepository {
    private static final Logger logger = LoggerConfig.getLogger(ArticleRepositoryImpl.class);
    private EntityManager entityManager;
    public ArticleRepositoryImpl() {
        this.entityManager = HibernateUtil.getEntityManager();
        logger.info("ArticleRepositoryImpl initialized with EntityManager");
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Article findById(Long id) {
        logger.debug("Finding article by ID: {}", id);
        Article article = entityManager.find(Article.class, id);
        logger.debug("Article found: {}", article != null);
        return article;
    }

    @Override
    public List<Article> findAll(int offset, int limit, String searchTitle) {
        logger.debug("Finding all articles - offset: {}, limit: {}, searchTitle: {}", offset, limit, searchTitle);
        String jpql = "FROM Article a WHERE :searchTitle IS NULL OR a.title LIKE :searchTitle ORDER BY a.creationDate DESC";
        List<Article> articles = entityManager.createQuery(jpql, Article.class)
                 .setParameter("searchTitle", searchTitle == null ? null : "%" + searchTitle + "%")
                 .setFirstResult(offset)
                 .setMaxResults(limit)
                 .getResultList();
        logger.debug("Query executed. Number of articles found: {}", articles.size());
        return articles;
    }

    @Override
    public void save(Article article) {
        logger.info("Saving new article: {}", article.getTitle());
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(article);
            transaction.commit();
            logger.info("Article saved successfully. ID: {}", article.getId());
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            logger.error("Error saving article: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void update(Article article) {
        logger.info("Updating article. ID: {}", article.getId());
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(article);
            transaction.commit();
            logger.info("Article updated successfully. ID: {}", article.getId());
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            logger.error("Error updating article: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void delete(Long id) {
        logger.info("Deleting article. ID: {}", id);
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Article article = entityManager.find(Article.class, id);
            if (article != null) {
                entityManager.remove(article);
                logger.info("Article deleted successfully. ID: {}", id);
            } else {
                logger.warn("Article not found for deletion. ID: {}", id);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            logger.error("Error deleting article: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public int getNoOfRecords(String searchTitle) {
        logger.debug("Getting number of records. SearchTitle: {}", searchTitle);
        String jpql = "SELECT COUNT(a) FROM Article a WHERE :searchTitle IS NULL OR a.title LIKE :searchTitle";
        int count = entityManager.createQuery(jpql, Long.class)
                .setParameter("searchTitle", searchTitle == null ? null : "%" + searchTitle + "%")
                .getSingleResult()
                .intValue();
        logger.debug("Number of records found: {}", count);
        return count;
    }

    public void close() {
        logger.info("Closing EntityManager");
        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
        }
    }
}