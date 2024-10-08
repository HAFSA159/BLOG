package com.blogapp.repository;

import java.util.List;

import com.blogapp.model.Article;

public interface ArticleRepository {
    Article findById(Long id);
    List<Article> findAll(int offset, int limit, String searchTitle);
    void save(Article article);
    void update(Article article);
    void delete(Long id);
    int getNoOfRecords(String searchTitle);
}
