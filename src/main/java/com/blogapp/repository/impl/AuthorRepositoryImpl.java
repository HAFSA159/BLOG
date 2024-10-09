package com.blogapp.repository.impl;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.blogapp.model.Author;
import com.blogapp.repository.AuthorRepository;
import com.blogapp.util.HibernateUtil;

public class AuthorRepositoryImpl implements AuthorRepository {
    private final EntityManager entityManager;

    public AuthorRepositoryImpl() {
        this.entityManager = HibernateUtil.getEntityManager();
    }

    @Override
    public Author findById(Long id) {
        return entityManager.find(Author.class, id);
    }

    @Override
    public List<Author> findAll() {
        return entityManager.createQuery("FROM Author", Author.class).getResultList();
    }

    @Override
    public void save(Author author) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(author);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to save the author", e);
        }
    }

    @Override
    public void update(Author author) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(author);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to update the author", e);
        }
    }

    @Override
    public void delete(Long id) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Author author = entityManager.find(Author.class, id);
            if (author != null) {
                entityManager.remove(author);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to delete the author", e);
        }
    }

    @Override
    public Author findByEmail(String email) {
        try {
            return entityManager.createQuery("FROM Author a WHERE a.email = :email", Author.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public void close() {
        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
        }
    }
}
