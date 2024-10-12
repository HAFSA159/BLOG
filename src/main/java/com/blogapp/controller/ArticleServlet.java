package com.blogapp.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import com.blogapp.model.Comment;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.logging.log4j.Logger;

import com.blogapp.config.LoggerConfig;
import com.blogapp.model.Article;
import com.blogapp.model.ArticleStatus;
import com.blogapp.model.Author;
import com.blogapp.repository.impl.ArticleRepositoryImpl;
import com.blogapp.repository.impl.AuthorRepositoryImpl;
import com.blogapp.repository.impl.CommentRepositoryImpl;
import com.blogapp.service.ArticleService;
import com.blogapp.service.AuthorService;
import com.blogapp.service.CommentService;

@WebServlet(name = "ArticleServlet", urlPatterns = { "/article/*" })
public class ArticleServlet extends HttpServlet {

    private static final Logger logger = LoggerConfig.getLogger(ArticleServlet.class);
    private ArticleService articleService;
    private AuthorService authorService;
    private CommentService commentService;
    private HashMap<Long, Article> articleCache = new HashMap<>(); // Using HashMap for caching

    @Override
    public void init() throws ServletException {
        super.init();
        articleService = new ArticleService(new ArticleRepositoryImpl());
        authorService = new AuthorService(new AuthorRepositoryImpl());
        commentService = new CommentService(new CommentRepositoryImpl());
        logger.info("ArticleServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        HttpSession session = request.getSession();
        String loggedInUser = (String) session.getAttribute("loggedInUser");

        switch (Optional.ofNullable(action).orElse("/")) { // Use Optional to avoid null checks
            case "/view":
                viewArticle(request, response);
                break;
            case "/list":
                if (loggedInUser == null) {
                    response.sendRedirect(request.getContextPath() + "/login.jsp");
                    return;
                }
                listArticles(request, response, loggedInUser);
                break;
            case "/create":
            case "/edit":
            case "/delete":
                if (loggedInUser == null) {
                    response.sendRedirect(request.getContextPath() + "/login.jsp");
                    return;
                }
                switch (action) {
                    case "/create":
                        showCreateForm(request, response);
                        break;
                    case "/edit":
                        showEditForm(request, response, loggedInUser);
                        break;
                    case "/delete":
                        deleteArticle(request, response);
                        break;
                }
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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

    private void listArticles(HttpServletRequest request, HttpServletResponse response, String loggedInUser)
            throws ServletException, IOException {
        int page = 1;
        int recordsPerPage = 5;
        String searchTitle = request.getParameter("searchTitle");

        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }

        List<Article> articles = articleService.getArticlesByAuthorEmail(loggedInUser, (page - 1) * recordsPerPage,
                recordsPerPage, searchTitle);

        // Sort articles by creation date in descending order using Stream API
        List<Article> sortedArticles = articles.stream()
                .sorted((a1, a2) -> a2.getCreationDate().compareTo(a1.getCreationDate()))
                .collect(Collectors.toList());

        int noOfRecords = articleService.getNumberOfRecordsByAuthorEmail(loggedInUser, searchTitle);
        int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);

        request.setAttribute("articles", sortedArticles); // Assign sorted articles
        request.setAttribute("noOfPages", noOfPages);
        request.setAttribute("currentPage", page);
        request.setAttribute("searchTitle", searchTitle);
        request.getRequestDispatcher("/WEB-INF/views/article/list.jsp").forward(request, response);
    }

    private void viewArticle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Long id = Long.parseLong(request.getParameter("id"));

            // Use cache to improve performance
            Article article = getArticleFromCache(id);

            if (article != null) {
                List<Comment> approvedComments = commentService.getApprovedCommentsByArticleId(id);
                article.setComments(approvedComments);
                request.setAttribute("article", article);
                request.getRequestDispatcher("/WEB-INF/views/article/view.jsp").forward(request, response);
            } else {
                request.setAttribute("errorMessage", "Article not found.");
                response.sendRedirect(request.getContextPath() + "/home");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid article ID.");
            response.sendRedirect(request.getContextPath() + "/home");
        } catch (Exception e) {
            logger.error("Error in viewArticle: ", e);
            request.setAttribute("errorMessage", "An unexpected error occurred.");
            response.sendRedirect(request.getContextPath() + "/home");
        }
    }

    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.debug("Showing create article form");
        List<Author> authors = authorService.getAllAuthors();
        request.setAttribute("authors", authors);
        request.getRequestDispatcher("/WEB-INF/views/article/create.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response, String loggedInUser)
            throws ServletException, IOException {
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            Optional<Article> optionalArticle = articleService.getArticleById(id);

            if (optionalArticle.isPresent()) {
                Article article = optionalArticle.get();
                // Check if the logged-in user is the author of the article
                if (!article.getAuthor().getEmail().equals(loggedInUser)) {
                    request.setAttribute("errorMessage", "You are not authorized to edit this article.");
                    listArticles(request, response, loggedInUser);
                    return;
                }

                request.setAttribute("article", article);
                List<Author> authors = authorService.getAllAuthors();
                request.setAttribute("authors", authors);
                request.getRequestDispatcher("/WEB-INF/views/article/edit.jsp").forward(request, response);
            } else {
                request.setAttribute("errorMessage", "Article not found.");
                listArticles(request, response, loggedInUser);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid article ID.");
            listArticles(request, response, loggedInUser);
        } catch (Exception e) {
            logger.error("Error in showEditForm: ", e);
            request.setAttribute("errorMessage", "An unexpected error occurred.");
            listArticles(request, response, loggedInUser);
        }
    }

    private void createArticle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String authorEmail = (String) request.getSession().getAttribute("loggedInUser");
        String status = request.getParameter("status");

        if (authorEmail == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User is not authenticated.");
            return;
        }

        Author author = authorService.getAuthorByEmail(authorEmail);
        if (author == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not found.");
            return;
        }

        Article newArticle = new Article();
        newArticle.setTitle(title);
        newArticle.setContent(content);
        newArticle.setAuthor(author);
        newArticle.setCreationDate(LocalDateTime.now());

        // Handle the status
        try {
            newArticle.setStatus(ArticleStatus.valueOf(status.toUpperCase()));
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid status provided: {}. Defaulting to PUBLISHED.", status);
            newArticle.setStatus(ArticleStatus.published);
        }

        try {
            logger.info("Attempting to add article: {}", newArticle);
            articleService.addArticle(newArticle);
            logger.info("Article added successfully");

            // Cache the new article
            cacheArticle(newArticle);

            response.sendRedirect(request.getContextPath() + "/article/list");
        } catch (Exception e) {
            logger.error("Error creating article: {}", e.getMessage(), e);
            request.setAttribute("error", "An error occurred while creating the article: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/article/create.jsp").forward(request, response);
        }
    }

    private void updateArticle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String status = request.getParameter("status");


        logger.info("Updating article - ID: {}, Title: {}, Status: {}", id, title, status);

        Article article = articleService.getArticleById(id);
        if (article == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Article not found");
            return;
        }

        // Check if the logged-in user is the author of the article
        String loggedInUser = (String) request.getSession().getAttribute("loggedInUser");
        if (!article.getAuthor().getEmail().equals(loggedInUser)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not authorized to edit this article");
            return;
        }

        article.setTitle(title);
        article.setContent(content);
        article.setStatus(ArticleStatus.valueOf(status.toLowerCase()));

        articleService.updateArticle(article);
        response.sendRedirect(request.getContextPath() + "/article/list");
    }

    private void deleteArticle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        articleService.deleteArticle(id);
        articleCache.remove(id); // Remove from cache after deletion
        response.sendRedirect(request.getContextPath() + "/article/list");
    }

            articleService.deleteArticle(id);
            logger.info("Article deleted successfully - ID: {}", id);
            String referer = request.getHeader("Referer");
            response.sendRedirect(referer != null ? referer : request.getContextPath() + "/article/list");
        } catch (Exception e) {
            logger.error("Error deleting article: {}", e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "An error occurred while deleting the article");
        }
    }


}
