package com.blogapp.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
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

@WebServlet(name = "ArticleServlet", urlPatterns = {"/article/*"})
@MultipartConfig
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

        switch (action) {
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
        int noOfRecords = articleService.getNumberOfRecordsByAuthorEmail(loggedInUser, searchTitle);
        int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);

        request.setAttribute("articles", articles);
        request.setAttribute("noOfPages", noOfPages);
        request.setAttribute("currentPage", page);
        request.setAttribute("searchTitle", searchTitle);
        request.getRequestDispatcher("/WEB-INF/views/article/list.jsp").forward(request, response);
    }

    private void viewArticle(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
try {
    Long id = Long.parseLong(request.getParameter("id"));
    Article article = articleService.getArticleById(id);

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
        newArticle.setStatus(ArticleStatus.valueOf(status.toLowerCase()));

        try {
            // Handle image upload
            Part filePart = request.getPart("image");
            if (filePart != null && filePart.getSize() > 0) {
                String fileName = getSubmittedFileName(filePart);
                String filePath = uploadFile(filePart, fileName);
                newArticle.setImagePath(filePath);
            }

            logger.info("Attempting to add article: {}", newArticle);
            articleService.addArticle(newArticle);
            logger.info("Article added successfully");
            response.sendRedirect(request.getContextPath() + "/article/list");
        } catch (IOException e) {
            logger.error("Error uploading file: {}", e.getMessage(), e);
            request.setAttribute("error", "An error occurred while uploading the image: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/article/create.jsp").forward(request, response);
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
        String status = request.getParameter("status");

        logger.info("Updating article - ID: {}, Title: {}, AuthorID: {}, Status: {}", id, title, authorId, status);

        Article article = articleService.getArticleById(id);
        article.setTitle(title);
        article.setContent(content);
        article.setAuthor(authorService.getAuthorById(authorId));
        article.setStatus(ArticleStatus.valueOf(status.toLowerCase()));

        // Handle image upload
        Part filePart = request.getPart("image");
        if (filePart != null && filePart.getSize() > 0) {
            String fileName = getSubmittedFileName(filePart);
            String filePath = uploadFile(filePart, fileName);
            article.setImagePath(filePath);
        }

        articleService.updateArticle(article);
        String referer = request.getHeader("Referer");
        response.sendRedirect(referer != null ? referer : request.getContextPath() + "/article/list");
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
            if (!article.getAuthor().getEmail().equals(loggedInUser)
                    && !"Editor".equals(session.getAttribute("userRole"))) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not authorized to delete this article");
                return;
            }

            articleService.deleteArticle(id);
            logger.info("Article deleted successfully - ID: {}", id);
            String referer = request.getHeader("Referer");
            response.sendRedirect(referer != null ? referer : request.getContextPath() + "/article/list");
        } catch (Exception e) {
            logger.error("Error deleting article: {}", e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while deleting the article");
        }
    }

    private String getSubmittedFileName(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return fileName.substring(fileName.lastIndexOf('/') + 1).substring(fileName.lastIndexOf('\\') + 1);
            }
        }
        return null;
    }

    private String uploadFile(Part filePart, String fileName) throws IOException {
        String projectPath = System.getProperty("user.dir");
        String uploadDir = projectPath + File.separator + "src" + File.separator + "main" + File.separator + "webapp" + File.separator + "uploads";
        File uploadDirFile = new File(uploadDir);
        if (!uploadDirFile.exists()) {
            if (!uploadDirFile.mkdirs()) {
                throw new IOException("Failed to create upload directory: " + uploadDir);
            }
        }
        String filePath = uploadDir + File.separator + fileName;
        try (InputStream inputStream = filePart.getInputStream();
             OutputStream outputStream = new FileOutputStream(filePath)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
        logger.info("File uploaded successfully: {}", filePath);
        return "/uploads/" + fileName;
    }
}