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
import java.util.Optional;

import org.apache.logging.log4j.Logger;

import com.blogapp.config.LoggerConfig;
import com.blogapp.model.Article;
import com.blogapp.model.Author;
import com.blogapp.model.Comment;
import com.blogapp.model.CommentStatus;
import com.blogapp.repository.impl.ArticleRepositoryImpl;
import com.blogapp.repository.impl.AuthorRepositoryImpl;
import com.blogapp.repository.impl.CommentRepositoryImpl;
import com.blogapp.service.ArticleService;
import com.blogapp.service.AuthorService;
import com.blogapp.service.CommentService;


@WebServlet(name = "CommentServlet", urlPatterns = {"/comment/*"})
public class CommentServlet extends HttpServlet {

    private static final Logger logger = LoggerConfig.getLogger(CommentServlet.class);
    private CommentService commentService;
    private ArticleService articleService;
    private AuthorService authorService;

    @Override
    public void init() throws ServletException {
        super.init();
        commentService = new CommentService(new CommentRepositoryImpl());
        articleService = new ArticleService(new ArticleRepositoryImpl());
        authorService = new AuthorService(new AuthorRepositoryImpl());
        logger.info("CommentServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();
        if (action == null) {
            action = "/list";
        }
        switch (action) {
            case "/list":
                listComments(request, response);
                break;
            case "/edit":
                editComment(request, response);
                break;

            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();
        switch (action) {
            case "/create":
                createComment(request, response);
                break;
            case "/edit":
                editComment(request, response);
                break;
            case "/delete":
                deleteComment(request, response);
                break;
            case "/updateStatus":
                updateCommentStatus(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }

    private void createComment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long articleId = Long.parseLong(request.getParameter("articleId"));
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
    
        Optional<Article> optionalArticle = articleService.getArticleById(articleId);
        if (!optionalArticle.isPresent()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Article not found.");
            return;
        }
    
        Article article = optionalArticle.get();
        Comment newComment = new Comment();
        newComment.setContent(content);
        newComment.setArticle(article);
        newComment.setAuthor(author);
        newComment.setCreationDate(LocalDateTime.now());
        newComment.setStatus(CommentStatus.approved);
    
        try {
            logger.info("Attempting to add comment: {}", newComment);
            commentService.addComment(newComment);
            logger.info("Comment added successfully");
    
            response.sendRedirect(request.getContextPath() + "/article/view?id=" + articleId);
        } catch (Exception e) {
            logger.error("Error creating comment: {}", e.getMessage(), e);
            logger.error("Stack trace: ", e);
            if (e.getCause() != null) {
                logger.error("Cause: {}", e.getCause().getMessage());
            }
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while adding the comment: " + e.getMessage());
        }
    }

    private void editComment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long commentId = Long.parseLong(request.getParameter("commentId"));
        String content = request.getParameter("content");
        String authorEmail = (String) request.getSession().getAttribute("loggedInUser");

        Comment comment = commentService.getCommentById(commentId);
        if (comment == null || !comment.getAuthor().getEmail().equals(authorEmail)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not authorized to edit this comment.");
            return;
        }

        comment.setContent(content);
        comment.setStatus(CommentStatus.approved);
        commentService.updateComment(comment);

        // Redirect back to the article view page
        Long articleId = comment.getArticle().getId();
        response.sendRedirect(request.getContextPath() + "/article/view?id=" + articleId);
    }

    private void deleteComment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long commentId = Long.parseLong(request.getParameter("commentId"));
        String authorEmail = (String) request.getSession().getAttribute("loggedInUser");

        Comment comment = commentService.getCommentById(commentId);
        if (comment == null || !comment.getAuthor().getEmail().equals(authorEmail)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not authorized to delete this comment.");
            return;
        }

        commentService.deleteComment(commentId);

        String referer = request.getHeader("Referer");
        if (referer != null && referer.contains("/article/view")) {
            // If the request came from the article view page, redirect back there
            response.sendRedirect(referer);
        } else {
            // Otherwise, redirect to the comment list page
            response.sendRedirect(request.getContextPath() + "/comment/list");
        }
    }

    private void updateCommentStatus(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String userRole = (String) session.getAttribute("userRole");
        logger.info("User role: {}", userRole);
        if (!"Editor".equals(userRole)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not authorized to update comment status.");
            return;
        }

        Long commentId = Long.parseLong(request.getParameter("commentId"));
        CommentStatus status = CommentStatus.valueOf(request.getParameter("status"));

        commentService.updateCommentStatus(commentId, status);

        String referer = request.getHeader("Referer");
        if (referer != null && referer.contains("/article/view")) {
            // If the request came from the article view page, redirect back there
            Long articleId = commentService.getCommentById(commentId).getArticle().getId();
            response.sendRedirect(referer != null ? referer : request.getContextPath() + "/article/view?id=" + articleId);
        } else {
            // Otherwise, redirect to the comment list page
            response.sendRedirect(request.getContextPath() + "/comment/list");
        }
    }

    private void listComments(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String loggedInUser = (String) session.getAttribute("loggedInUser");
        String userRole = (String) session.getAttribute("userRole");

        List<Comment> comments;
        try {
            if ("Editor".equals(userRole)) {
                comments = commentService.getAllComments();
            } else {
                comments = commentService.getCommentsByAuthorEmail(loggedInUser);
            }
            request.setAttribute("comments", comments);
            request.getRequestDispatcher("/WEB-INF/views/comment/list.jsp").forward(request, response);
        } catch (Exception e) {
            logger.error("Error fetching comments: ", e);
            request.setAttribute("errorMessage", "An error occurred while fetching comments. Please try again later.");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
}
