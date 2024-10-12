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
import com.blogapp.model.Comment;
import com.blogapp.repository.impl.CommentRepositoryImpl;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CommentRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private EntityTransaction transaction;

    @Mock
    private TypedQuery<Comment> query;

    private CommentRepositoryImpl commentRepository;

    @Before
    public void setUp() {
        commentRepository = new CommentRepositoryImpl();
        commentRepository.setEntityManager(entityManager);
        when(entityManager.getTransaction()).thenReturn(transaction);
    }

    @Test
    public void testSave() {
        Comment comment = new Comment();
        comment.setContent("Test Comment");
        
        Article article = new Article();
        article.setId(1L);
        comment.setArticle(article);

        when(entityManager.getTransaction()).thenReturn(transaction);

        commentRepository.save(comment);

        verify(transaction).begin();
        verify(entityManager).persist(comment);
        verify(transaction).commit();
    }

    @Test
    public void testUpdate() {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setContent("Updated Comment");

        commentRepository.update(comment);

        verify(transaction).begin();
        verify(entityManager).merge(comment);
        verify(transaction).commit();
    }

    @Test
    public void testFindById() {
        Long commentId = 1L;
        Comment expectedComment = new Comment();
        expectedComment.setId(commentId);

        when(entityManager.find(Comment.class, commentId)).thenReturn(expectedComment);

        Comment actualComment = commentRepository.findById(commentId);

        assertEquals(expectedComment, actualComment);
    }

    @Test
    public void testFindAllByArticleId() {
        Long articleId = 1L;
        Comment comment1 = new Comment();
        Comment comment2 = new Comment();
        List<Comment> expectedComments = Arrays.asList(comment1, comment2);

        when(entityManager.createQuery(anyString(), eq(Comment.class))).thenReturn(query);
        when(query.setParameter("articleId", articleId)).thenReturn(query);
        when(query.getResultList()).thenReturn(expectedComments);

        List<Comment> actualComments = commentRepository.findAllByArticleId(articleId);

        assertEquals(expectedComments, actualComments);
        verify(query).setParameter("articleId", articleId);
    }

    @Test
    public void testFindByAuthorEmail() {
        String authorEmail = "test@example.com";
        Comment comment1 = new Comment();
        Comment comment2 = new Comment();
        List<Comment> expectedComments = Arrays.asList(comment1, comment2);

        when(entityManager.createQuery(anyString(), eq(Comment.class))).thenReturn(query);
        when(query.setParameter("email", authorEmail)).thenReturn(query);
        when(query.getResultList()).thenReturn(expectedComments);

        List<Comment> actualComments = commentRepository.findByAuthorEmail(authorEmail);

        assertEquals(expectedComments, actualComments);
        verify(query).setParameter("email", authorEmail);
    }

    @Test
    public void testDelete() {
        Long commentId = 1L;
        Comment comment = new Comment();
        comment.setId(commentId);

        when(entityManager.find(Comment.class, commentId)).thenReturn(comment);

        commentRepository.delete(commentId);

        verify(transaction).begin();
        verify(entityManager).remove(comment);
        verify(transaction).commit();
    }
}
