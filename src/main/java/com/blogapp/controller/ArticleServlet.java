package com.blogapp.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.Logger;

import com.blogapp.config.LoggerConfig;
import com.blogapp.model.Article;
import com.blogapp.model.ArticleStatus;
import com.blogapp.model.Author;
import com.blogapp.model.Comment;
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

        if (loggedInUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        switch (action) {
            case "/list":
                listArticles(request, response, loggedInUser);
                break;
            case "/view":
                viewArticle(request, response, loggedInUser);
                break;
            case "/create":
                showCreateForm(request, response);
                break;
            case "/edit":
                showEditForm(request, response, loggedInUser);
                break;
            case "/delete":
                deleteArticle(request, response);
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
        int noOfRecords = articleService.getNumberOfRecordsByAuthorEmail(loggedInUser, searchTitle);
        int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);

        request.setAttribute("articles", articles);
        request.setAttribute("noOfPages", noOfPages);
        request.setAttribute("currentPage", page);
        request.setAttribute("searchTitle", searchTitle);
        request.getRequestDispatcher("/WEB-INF/views/article/list.jsp").forward(request, response);
    }

    private void viewArticle(HttpServletRequest request, HttpServletResponse response, String loggedInUser)
            throws ServletException, IOException {
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            logger.debug("Viewing article with ID: {}", id);
            Article article = articleService.getArticleById(id);

            if (article == null) {
                request.setAttribute("errorMessage", "Article not found.");
                listArticles(request, response, loggedInUser);
                return;
            }
            
            // Check if the logged-in user is the author of the article
            if (!article.getAuthor().getEmail().equals(loggedInUser)) {
                request.setAttribute("errorMessage", "You are not authorized to view this article.");
                listArticles(request, response, loggedInUser);
                return;
            }
            
            // Fetch the latest comments for the article
            List<Comment> comments = commentService.getAllCommentsByArticleId(article.getId());
            article.setComments(comments);
            
            request.setAttribute("article", article);
            request.getRequestDispatcher("/WEB-INF/views/article/view.jsp").forward(request, response); 
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid article ID.");
            request.setAttribute("errorMessage", "Invalid article ID.");
            listArticles(request, response, loggedInUser);
        } catch (Exception e) {
            logger.error("Error in viewArticle: ", e);
            request.setAttribute("errorMessage", "An unexpected error occurred.");
            listArticles(request, response, loggedInUser);
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
            Article article = articleService.getArticleById(id);

            if (article != null) {
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
        newArticle.setStatus(ArticleStatus.draft);

        try {
            logger.info("Attempting to add article: {}", newArticle);
            articleService.addArticle(newArticle);
            logger.info("Article added successfully");
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

    private void deleteArticle(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
HttpSession session = request.getSession();
String loggedInUser = (String) session.getAttribute("loggedInUser");

if (loggedInUser == null) {
    response.sendRedirect(request.getContextPath() + "/login.jsp");
    return;
}

Long id = Long.parseLong(request.getParameter("id"));
logger.info("Deleting article with ID: {}", id);

try {
    Article article = articleService.getArticleById(id);
    if (article == null) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Article not found");
        return;
    }

    // Check if the logged-in user is the author of the article or an admin
    if (!article.getAuthor().getEmail().equals(loggedInUser) && 
        !"Editor".equals(session.getAttribute("userRole"))) {
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not authorized to delete this article");
        return;
    }

    articleService.deleteArticle(id);
    logger.info("Article deleted successfully - ID: {}", id);
    response.sendRedirect(request.getContextPath() + "/article/list");
} catch (Exception e) {
    logger.error("Error deleting article: {}", e.getMessage(), e);
    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while deleting the article");
}
}
}