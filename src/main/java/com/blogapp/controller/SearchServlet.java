package com.blogapp.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.blogapp.model.Article;
import com.blogapp.repository.impl.ArticleRepositoryImpl;
import com.blogapp.service.ArticleService;

@WebServlet(name = "SearchServlet", urlPatterns = {"", "/home", "/search"})
public class SearchServlet extends HttpServlet {

    private ArticleService articleService;

    @Override
    public void init() throws ServletException {
        super.init();
        articleService = new ArticleService(new ArticleRepositoryImpl());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");
        int page = 1;
        int recordsPerPage = 5;

        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }

        List<Article> articles = articleService.getPublishedArticles((page - 1) * recordsPerPage, recordsPerPage, query);
        int noOfRecords = articleService.getNoOfPublishedRecords(query);

        int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);

        request.setAttribute("articles", articles);
        request.setAttribute("noOfPages", noOfPages);
        request.setAttribute("currentPage", page);
        request.setAttribute("query", query);
        request.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(request, response);
    }
}
