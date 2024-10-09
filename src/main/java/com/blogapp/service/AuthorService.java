package com.blogapp.service;

import java.util.List;
import org.mindrot.jbcrypt.BCrypt;
import com.blogapp.model.Author;
import com.blogapp.repository.AuthorRepository;

public class AuthorService {
    private final AuthorRepository authorRepository;

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
        if (authorRepository.findByEmail(author.getEmail()) == null) {
            authorRepository.save(author);
        } else {
            throw new IllegalArgumentException("An author with this email already exists.");
        }
    }

    public void updateAuthor(Author author) {
        if (authorRepository.findById(author.getId()) != null) {
            authorRepository.update(author);
        } else {
            throw new IllegalArgumentException("Author not found for the given ID.");
        }
    }

    public void deleteAuthor(Long id) {
        if (authorRepository.findById(id) != null) {
            authorRepository.delete(id);
        } else {
            throw new IllegalArgumentException("Author not found for the given ID.");
        }
    }

    public Author findAuthorByEmail(String email) {
        return authorRepository.findByEmail(email);
    }

    public boolean authenticate(String email, String password) {
        Author author = findAuthorByEmail(email);
        // Verify the entered password against the hashed password stored in the database
        if (author != null && BCrypt.checkpw(password, author.getPassword())) {
            return true;
        }
        return false;
    }

    public AuthorRepository getAuthorRepository() {
        return authorRepository;
    }
}
