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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();
        switch (action) {
            case "/create":
                createComment(request, response);
                break;
            case "/manage":
                manageComments(request, response);
                break;
            case "/updateStatus":
                updateCommentStatus(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }
    private void manageComments(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Comment> comments = commentService.getAllComments();
        request.setAttribute("comments", comments);
        request.getRequestDispatcher("/WEB-INF/views/comment/manage.jsp").forward(request, response);
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
    
        Comment newComment = new Comment();
        newComment.setContent(content);
        newComment.setArticle(articleService.getArticleById(articleId));
        newComment.setAuthor(author);
        newComment.setCreationDate(LocalDateTime.now());
        newComment.setStatus(CommentStatus.approved);
    
        try {
            logger.info("Attempting to add comment: {}", newComment);
            commentService.addComment(newComment);
            logger.info("Comment added successfully");
            
            // Fetch updated article with comments
            request.setAttribute("article", articleService.getArticleById(articleId));
            request.getRequestDispatcher("/WEB-INF/views/article/view.jsp").forward(request, response);
        } catch (Exception e) {
            logger.error("Error creating comment: {}", e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while adding the comment: " + e.getMessage());
        }
    }

    private void updateCommentStatus(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        String status = request.getParameter("status");
        
        try {
            commentService.updateCommentStatus(id, CommentStatus.valueOf(status.toUpperCase()));
            response.sendRedirect(request.getContextPath() + "/comment/manage");
        } catch (Exception e) {
            logger.error("Error updating comment status: {}", e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while updating the comment status.");
        }
    }
}
