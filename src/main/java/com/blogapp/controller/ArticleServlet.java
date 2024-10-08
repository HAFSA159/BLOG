package com.blogapp.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.blogapp.model.Article;
import com.blogapp.model.ArticleStatus;
import com.blogapp.model.Author;
import com.blogapp.repository.impl.ArticleRepositoryImpl;
import com.blogapp.repository.impl.AuthorRepositoryImpl;
import com.blogapp.service.ArticleService;
import com.blogapp.service.AuthorService;

@WebServlet(name = "ArticleServlet", urlPatterns = {"/article/*"})
public class ArticleServlet extends HttpServlet {
    private ArticleService articleService;
    private AuthorService authorService;

    @Override
    public void init() throws ServletException {
        super.init();
        articleService = new ArticleService(new ArticleRepositoryImpl());
        authorService = new AuthorService(new AuthorRepositoryImpl());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();
        if (action == null) {
            action = "/list";
        }

        switch (action) {
            case "/list":
                listArticles(request, response);
                break;
            case "/view":
                viewArticle(request, response);
                break;
            case "/create":
                showCreateForm(request, response);
                break;
            case "/edit":
                showEditForm(request, response);
                break;
            default:
                listArticles(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();

        switch (action) {
            case "/create":
                createArticle(request, response);
                break;
            case "/edit":
                updateArticle(request, response);
                break;
            case "/delete":
                deleteArticle(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/article/list");
                break;
        }
    }

    private void listArticles(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int page = 1;
        int recordsPerPage = 10;
        String searchTitle = request.getParameter("searchTitle");
        
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }
        
        List<Article> articles = articleService.getAllArticles((page - 1) * recordsPerPage, recordsPerPage, searchTitle);
        int noOfRecords = articleService.getNoOfRecords(searchTitle);
        int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
        
        request.setAttribute("articles", articles);
        request.setAttribute("noOfPages", noOfPages);
        request.setAttribute("currentPage", page);
        request.setAttribute("searchTitle", searchTitle);
        request.getRequestDispatcher("/WEB-INF/views/article/list.jsp").forward(request, response);
    }

    private void viewArticle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        Article article = articleService.getArticleById(id);
        request.setAttribute("article", article);
        request.getRequestDispatcher("/WEB-INF/views/article/view.jsp").forward(request, response);
    }

    private void showCreateForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Author> authors = authorService.getAllAuthors();
        request.setAttribute("authors", authors);
        request.getRequestDispatcher("/WEB-INF/views/article/create.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        Article article = articleService.getArticleById(id);
        List<Author> authors = authorService.getAllAuthors();
        request.setAttribute("article", article);
        request.setAttribute("authors", authors);
        request.getRequestDispatcher("/WEB-INF/views/article/edit.jsp").forward(request, response);
    }

 private void createArticle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String title = request.getParameter("title");
    String content = request.getParameter("content");
    Long authorId = Long.parseLong(request.getParameter("authorId"));

        Article newArticle = new Article();
        newArticle.setTitle(title);
        newArticle.setContent(content);
        newArticle.setAuthor(authorService.getAuthorById(authorId));
        newArticle.setCreationDate(LocalDateTime.now());
        newArticle.setStatus(ArticleStatus.draft);

    articleService.addArticle(newArticle);
    response.sendRedirect(request.getContextPath() + "/article/list");
}

    private void updateArticle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        Long authorId = Long.parseLong(request.getParameter("authorId"));

        Article article = articleService.getArticleById(id);
        article.setTitle(title);
        article.setContent(content);
        article.setAuthor(authorService.getAuthorById(authorId));

        articleService.updateArticle(article);
        response.sendRedirect(request.getContextPath() + "/article/view?id=" + id);
    }

    private void deleteArticle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        articleService.deleteArticle(id);
        response.sendRedirect(request.getContextPath() + "/article/list");
    }
}