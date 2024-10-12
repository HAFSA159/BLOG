package com.blogapp.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.blogapp.model.Article;
import com.blogapp.model.ArticleStatus;
import com.blogapp.repository.ArticleRepository;

public class ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    // Get article by id using Optional to handle possible null values
    public Optional<Article> getArticleById(Long id) {
        return articleRepository.findById(id);
    }

    // Return a paginated list of articles, optionally filtering by title
    public List<Article> getAllArticles(int offset, int limit, String searchTitle) {
        return articleRepository.findAll(offset, limit, searchTitle);
    }

    // Add a new article to the repository
    public void addArticle(Article article) {
        articleRepository.save(article);
    }

    // Update an existing article
    public void updateArticle(Article article) {
        articleRepository.update(article);
    }

    // Delete an article by id
    public void deleteArticle(Long id) {
        articleRepository.delete(id);
    }

    // Get the number of records based on a search title (supports pagination)
    public int getNoOfRecords(String searchTitle) {
        return articleRepository.getNoOfRecords(searchTitle);
    }

    // Fetch articles by author email with optional title filtering
    public List<Article> getArticlesByAuthorEmail(String authorEmail, int offset, int limit, String searchTitle) {
        return articleRepository.getArticlesByAuthorEmail(authorEmail, offset, limit, searchTitle);
    }

    // Get the total number of records for an author by their email and optional search title
    public int getNumberOfRecordsByAuthorEmail(String authorEmail, String searchTitle) {
        return articleRepository.getNumberOfRecordsByAuthorEmail(authorEmail, searchTitle);
    }
    public List<Article> getPublishedArticles(int offset, int limit, String searchTitle) {
        return articleRepository.getPublishedArticles(offset, limit, searchTitle);
    }
    
    public int getNoOfPublishedRecords(String searchTitle) {
        return articleRepository.getNoOfPublishedRecords(searchTitle);
    }
}
