package com.blogapp.service;

import java.util.List;

import com.blogapp.model.Article;
import com.blogapp.repository.ArticleRepository;

public class ArticleService {
    private ArticleRepository articleRepository;
 
    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public Article getArticleById(Long id) {
        return articleRepository.findById(id);
    }

    public List<Article> getAllArticles(int offset, int limit, String searchTitle) {
        return articleRepository.findAll(offset, limit, searchTitle);
    }

    public void addArticle(Article article) {
        articleRepository.save(article);
    }

    public void updateArticle(Article article) {
        articleRepository.update(article);
    }

    public void deleteArticle(Long id) {
        articleRepository.delete(id);
    }

    public int getNoOfRecords(String searchTitle) {
        return articleRepository.getNoOfRecords(searchTitle);
    }

    public ArticleRepository getArticleRepository() {
        return articleRepository;
    }
}
