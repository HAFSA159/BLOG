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

import com.blogapp.model.Author;
import com.blogapp.model.AuthorRole;
import com.blogapp.repository.impl.AuthorRepositoryImpl;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AuthorRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private EntityTransaction transaction;

    @Mock
    private TypedQuery<Author> query;

    private AuthorRepositoryImpl authorRepository;

    @Before
    public void setUp() {
        authorRepository = new AuthorRepositoryImpl();
        authorRepository.setEntityManager(entityManager);
        when(entityManager.getTransaction()).thenReturn(transaction);
    }

    @Test
    public void testSave() {
        Author author = new Author();
        author.setName("Test Author");

        authorRepository.save(author);

        verify(transaction).begin();
        verify(entityManager).persist(author);
        verify(transaction).commit();
    }

    @Test
    public void testUpdate() {
        Author author = new Author();
        author.setId(1L);
        author.setName("Updated Author");

        authorRepository.update(author);

        verify(transaction).begin();
        verify(entityManager).merge(author);
        verify(transaction).commit();
    }

    @Test
    public void testFindAll() {
        Author author1 = new Author();
        author1.setName("Author 1");
        Author author2 = new Author();
        author2.setName("Author 2");
        List<Author> expectedAuthors = Arrays.asList(author1, author2);

        when(entityManager.createQuery("FROM Author", Author.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(expectedAuthors);

        List<Author> actualAuthors = authorRepository.findAll();

        assertEquals(expectedAuthors, actualAuthors);
    }

    @Test
    public void testFindById() {
        Long authorId = 1L;
        Author expectedAuthor = new Author();
        expectedAuthor.setId(authorId);

        when(entityManager.find(Author.class, authorId)).thenReturn(expectedAuthor);

        Author actualAuthor = authorRepository.findById(authorId);

        assertEquals(expectedAuthor, actualAuthor);
    }

    @Test
    public void testUpdateRole() {
        Long authorId = 1L;
        AuthorRole newRole = AuthorRole.Editor;
        Author author = new Author();
        author.setId(authorId);

        when(entityManager.find(Author.class, authorId)).thenReturn(author);

        authorRepository.updateRole(authorId, newRole);

        verify(transaction).begin();
        verify(entityManager).merge(author);
        verify(transaction).commit();
        assertEquals(newRole, author.getRole());
    }

    @Test
    public void testDelete() {
        Long authorId = 1L;
        Author author = new Author();
        author.setId(authorId);

        when(entityManager.find(Author.class, authorId)).thenReturn(author);

        authorRepository.delete(authorId);

        verify(transaction).begin();
        verify(entityManager).remove(author);
        verify(transaction).commit();
    }
}