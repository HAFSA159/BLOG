package com.blogapp.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;

import com.blogapp.config.LoggerConfig;
import com.blogapp.model.Article;
import com.blogapp.model.ArticleStatus;
import com.blogapp.model.Author;
import com.blogapp.repository.impl.ArticleRepositoryImpl;
import com.blogapp.repository.impl.AuthorRepositoryImpl;
import com.blogapp.service.ArticleService;
import com.blogapp.service.AuthorService;

@WebServlet(name = "ArticleServlet", urlPatterns = {"/article/*"})
public class ArticleServlet extends HttpServlet {
    private static final Logger logger = LoggerConfig.getLogger(ArticleServlet.class);
    private ArticleService articleService;
    private AuthorService authorService;

    @Override
    public void init() throws ServletException {
        super.init();
        articleService = new ArticleService(new ArticleRepositoryImpl());
        authorService = new AuthorService(new AuthorRepositoryImpl());
        logger.info("ArticleServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();
        if (action == null) {
            action = "/list";
        }
        logger.debug("doGet action: {}", action);

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
                logger.warn("Unknown action: {}", action);
                listArticles(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();
        logger.debug("doPost action: {}", action);

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
                logger.warn("Unknown action: {}", action);
                response.sendRedirect(request.getContextPath() + "/article/list");
                break;
        }
    }

    private void listArticles(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int page = 1;
        int recordsPerPage = 9;
        String searchTitle = request.getParameter("searchTitle");
        
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }
        
        logger.debug("Listing articles - Page: {}, SearchTitle: {}", page, searchTitle);
        
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
        logger.debug("Viewing article with ID: {}", id);
        Article article = articleService.getArticleById(id);
        request.setAttribute("article", article);
        request.getRequestDispatcher("/WEB-INF/views/article/view.jsp").forward(request, response);
    }

    private void showCreateForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.debug("Showing create article form");
        List<Author> authors = authorService.getAllAuthors();
        request.setAttribute("authors", authors);
        request.getRequestDispatcher("/WEB-INF/views/article/create.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        logger.debug("Showing edit form for article with ID: {}", id);
        Article article = articleService.getArticleById(id);

        // Define offset and limit for pagination
        int offset = 0; // Default offset
        int limit = 10; // Default limit

        // Get authors using the required parameters
        List<Author> authors = authorService.getAllAuthors();

        request.setAttribute("article", article);
        request.setAttribute("authors", authors);

        request.getRequestDispatcher("/WEB-INF/views/article/edit.jsp").forward(request, response);
    }


    private void createArticle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String title = request.getParameter("title");
    String content = request.getParameter("content");
    Long authorId = Long.parseLong(request.getParameter("authorId"));

    private void createArticle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        Long authorId = Long.parseLong(request.getParameter("authorId"));

        logger.info("Creating new article - Title: {}, AuthorID: {}", title, authorId);

        Article newArticle = new Article();
        newArticle.setTitle(title);
        newArticle.setContent(content);
        newArticle.setAuthor(authorService.getAuthorById(authorId));
        newArticle.setCreationDate(LocalDateTime.now());
        newArticle.setStatus(ArticleStatus.draft);

        articleService.addArticle(newArticle);
        logger.info("Article created successfully - ID: {}", newArticle.getId());
        response.sendRedirect(request.getContextPath() + "/article/list");
    }

    private void updateArticle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        Long authorId = Long.parseLong(request.getParameter("authorId"));

        logger.info("Updating article - ID: {}, Title: {}, AuthorID: {}", id, title, authorId);

        Article article = articleService.getArticleById(id);
        article.setTitle(title);
        article.setContent(content);
        article.setAuthor(authorService.getAuthorById(authorId));

        articleService.updateArticle(article);
        logger.info("Article updated successfully - ID: {}", id);
        response.sendRedirect(request.getContextPath() + "/article/view?id=" + id);
    }

    private void deleteArticle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        logger.info("Deleting article with ID: {}", id);
        articleService.deleteArticle(id);
        logger.info("Article deleted successfully - ID: {}", id);
        response.sendRedirect(request.getContextPath() + "/article/list");
    }
}
}