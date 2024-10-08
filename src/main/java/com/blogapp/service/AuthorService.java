package com.blogapp.service;

import java.util.List;

import com.blogapp.model.Author;
import com.blogapp.repository.AuthorRepository;

public class AuthorService {
    private AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author getAuthorById(Long id) {
        return authorRepository.findById(id);
    }

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public void addAuthor(Author author) {
        authorRepository.save(author);
    }

    public void updateAuthor(Author author) {
        authorRepository.update(author);
    }

    public void deleteAuthor(Long id) {
        authorRepository.delete(id);
    }

    public AuthorRepository getAuthorRepository() {
        return authorRepository;
    }
}
