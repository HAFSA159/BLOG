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

    // Fetch all articles by an author with title search (using Stream API)
    public List<Article> searchArticlesByTitle(String searchTitle) {
        return articleRepository.findAll(0, Integer.MAX_VALUE, null)
                .stream()
                .filter(article -> article.getTitle().toLowerCase().contains(searchTitle.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Get all articles for the author and sort by creation date (using Streams)
    public List<Article> getSortedArticlesByAuthorEmail(String authorEmail, String searchTitle) {
        return articleRepository.getArticlesByAuthorEmail(authorEmail, 0, Integer.MAX_VALUE, searchTitle)
                .stream()
                .sorted((a1, a2) -> a2.getCreationDate().compareTo(a1.getCreationDate())) // Descending order
                .collect(Collectors.toList());
    }

    // Get a list of articles that have been published (status = PUBLISHED)
    public List<Article> getPublishedArticles() {
        return articleRepository.findAll(0, Integer.MAX_VALUE, null)
                .stream()
                .filter(article -> article.getStatus() == ArticleStatus.published)
                .collect(Collectors.toList());
    }

    public ArticleRepository getArticleRepository() {
        return articleRepository;
    }
}
