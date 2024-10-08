package com.blogapp.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.blogapp.model.Article;
import com.blogapp.repository.ArticleRepository;
import com.blogapp.util.HibernateUtil;

public class ArticleRepositoryImpl implements ArticleRepository {
    private final EntityManager entityManager;

    public ArticleRepositoryImpl() {
        this.entityManager = HibernateUtil.getEntityManager();
    }

    @Override
    public Article findById(Long id) {
        return entityManager.find(Article.class, id);
    }

    @Override
    public List<Article> findAll(int offset, int limit, String searchTitle) {
        String jpql = "FROM Article a WHERE :searchTitle IS NULL OR a.title LIKE :searchTitle ORDER BY a.creationDate DESC";
        List<Article> articles = entityManager.createQuery(jpql, Article.class)
                 .setParameter("searchTitle", searchTitle == null ? null : "%" + searchTitle + "%")
                 .setFirstResult(offset)
                 .setMaxResults(limit)
                 .getResultList();
        System.out.println("Query executed. Number of articles found: " + articles.size());
        return articles;
    }
    @Override
    public void save(Article article) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(article);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    @Override
    public void update(Article article) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(article);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    @Override
    public void delete(Long id) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Article article = entityManager.find(Article.class, id);
            if (article != null) {
                entityManager.remove(article);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    @Override
    public int getNoOfRecords(String searchTitle) {
        String jpql = "SELECT COUNT(a) FROM Article a WHERE :searchTitle IS NULL OR a.title LIKE :searchTitle";
        return entityManager.createQuery(jpql, Long.class)
                .setParameter("searchTitle", searchTitle == null ? null : "%" + searchTitle + "%")
                .getSingleResult()
                .intValue();
    }
    public void close() {
        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
        }
    }
}
