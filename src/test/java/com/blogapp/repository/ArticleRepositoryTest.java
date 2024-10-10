package com.blogapp.repository;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.Arrays;
import java.util.List;

import com.blogapp.model.Article;
import com.blogapp.repository.impl.ArticleRepositoryImpl;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ArticleRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private EntityTransaction transaction;

    @Mock
    private TypedQuery<Article> query;

    private ArticleRepositoryImpl articleRepository;

    @Before
    public void setUp() {
        articleRepository = new ArticleRepositoryImpl();
        articleRepository.setEntityManager(entityManager);
        when(entityManager.getTransaction()).thenReturn(transaction);
    }

    @Test
    public void testSave() {
        Article article = new Article();
        article.setTitle("Test Article");

        articleRepository.save(article);

        verify(transaction).begin();
        verify(entityManager).persist(article);
        verify(transaction).commit();
    }

    @Test
    public void testUpdate() {
        Article article = new Article();
        article.setId(1L);
        article.setTitle("Updated Article");

        articleRepository.update(article);

        verify(transaction).begin();
        verify(entityManager).merge(article);
        verify(transaction).commit();
    }

    @Test
    public void testFindAll() {
        int offset = 0;
        int limit = 10;
        String searchTitle = "Test";

        Article article1 = new Article();
        article1.setTitle("Test Article 1");
        Article article2 = new Article();
        article2.setTitle("Test Article 2");
        List<Article> expectedArticles = Arrays.asList(article1, article2);

        when(entityManager.createQuery(anyString(), eq(Article.class))).thenReturn(query);
        when(query.setParameter(eq("searchTitle"), anyString())).thenReturn(query);
        when(query.setFirstResult(offset)).thenReturn(query);
        when(query.setMaxResults(limit)).thenReturn(query);
        when(query.getResultList()).thenReturn(expectedArticles);

        List<Article> actualArticles = articleRepository.findAll(offset, limit, searchTitle);

        assertEquals(expectedArticles, actualArticles);
        verify(query).setParameter("searchTitle", "%" + searchTitle + "%");
        verify(query).setFirstResult(offset);
        verify(query).setMaxResults(limit);
    }
}
