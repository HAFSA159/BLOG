package com.blogapp.repository;

import java.util.List;

import com.blogapp.model.Author;

public interface AuthorRepository {
    Author findById(Long id);
    List<Author> findAll();
    void save(Author author);
    Author findByEmail(String email);
    void update(Author author);
    void delete(Long id);
}
