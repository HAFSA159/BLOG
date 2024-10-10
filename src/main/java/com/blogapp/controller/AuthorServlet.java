package com.blogapp.controller;

import com.blogapp.model.Author;
import com.blogapp.model.AuthorRole;
import com.blogapp.service.AuthorService;
import com.blogapp.repository.impl.AuthorRepositoryImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@WebServlet(name = "AuthorServlet", value = "/author/*")
public class AuthorServlet extends HttpServlet {
    private AuthorService authorService;

    @Override
    public void init() throws ServletException {
        // Initialize the AuthorService with the AuthorRepository
        authorService = new AuthorService(new AuthorRepositoryImpl());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        // Map of action handlers
        Map<String, Runnable> actionHandlers = new HashMap<>();
        actionHandlers.put("list", () -> listAuthors(request, response));
        actionHandlers.put("changeRole", () -> changeRole(request, response));
        actionHandlers.put("edit", () -> showEditForm(request, response));
        actionHandlers.put("create",()->showAddForm(request, response));
        actionHandlers.put(null, () -> listAuthors(request, response));

        actionHandlers.getOrDefault(action, () -> {
            throw new IllegalArgumentException("Invalid action");
        }).run();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getParameter("action");

        // Handle delete action separately in POST
        if ("delete".equalsIgnoreCase(action)) {
            deleteAuthor(request, response);
        } else if ("changeRole".equalsIgnoreCase(action)) {
            changeRole(request, response);
        } else if ("update".equalsIgnoreCase(action)) { // New condition for update action
            try {
                updateAuthor(request);
                response.sendRedirect(request.getContextPath() + "/author?action=list");
            } catch (IllegalArgumentException e) {
                request.setAttribute("errorMessage", e.getMessage());
                showEditForm(request, response); // Show the edit form in case of error
            } catch (Exception e) {
                request.setAttribute("errorMessage", "An unexpected error occurred. Please try again.");
                showEditForm(request, response); // Show the edit form in case of unexpected error
            }
        } else {
            try {
                Long authorId = Long.valueOf(request.getParameter("authorId"));
                AuthorRole newRole = AuthorRole.valueOf(request.getParameter("newRole"));

                authorService.changeAuthorRole(authorId, newRole);
                response.sendRedirect(request.getContextPath() + "/author?action=list");
            } catch (IllegalArgumentException e) {
                request.setAttribute("errorMessage", "Invalid role specified.");
                listAuthors(request, response);
            } catch (Exception e) {
                request.setAttribute("errorMessage", "An unexpected error occurred. Please try again.");
                listAuthors(request, response);
            }
        }
    }
    private void listAuthors(HttpServletRequest request, HttpServletResponse response) {
        List<Author> authors = authorService.getAllAuthors();
        request.setAttribute("authors", authors);

        try {
            request.getRequestDispatcher("/WEB-INF/views/author/authorList.jsp").forward(request, response);
        } catch (ServletException | IOException e) {
            throw new RuntimeException("Error while listing authors.", e);
        }
    }

    private void addAuthor(HttpServletRequest request) {
        Author author = new Author();
        author.setName(request.getParameter("name"));
        author.setEmail(request.getParameter("email"));
        author.setPassword(request.getParameter("password"));

        String birthdateStr = request.getParameter("birthdate");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate birthdate = LocalDate.parse(birthdateStr, formatter);
            author.setBirthdate(birthdate);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid birthdate format. Please use yyyy-MM-dd.");
        }

        setAuthorRole(request, author);
        authorService.addAuthor(author);
    }

    private void updateAuthor(HttpServletRequest request) {
        Long id = Long.parseLong(request.getParameter("authorId")); // Use the parameter from the form
        Author author = authorService.getAuthorById(id);

        if (author != null) {
            author.setName(request.getParameter("name"));
            author.setEmail(request.getParameter("email"));

            String birthdateStr = request.getParameter("birthdate");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            try {
                LocalDate birthdate = LocalDate.parse(birthdateStr, formatter);
                author.setBirthdate(birthdate);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid birthdate format. Please use yyyy-MM-dd.");
            }

            authorService.updateAuthor(author);
        } else {
            throw new IllegalArgumentException("Author not found for the given ID.");
        }
    }
    private void deleteAuthor(HttpServletRequest request, HttpServletResponse response) {
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            authorService.deleteAuthor(id);
            response.sendRedirect(request.getContextPath() + "/author?action=list");
        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", e.getMessage());
            listAuthors(request, response);
        } catch (IOException e) {
            throw new RuntimeException("Error redirecting after deletion", e);
        }
    }

    private void changeRole(HttpServletRequest request, HttpServletResponse response) {
        try {
            Long authorId = Long.parseLong(request.getParameter("id"));
            AuthorRole newRole = AuthorRole.valueOf(request.getParameter("role").toUpperCase());
            authorService.changeAuthorRole(authorId, newRole);
            response.sendRedirect(request.getContextPath() + "/author?action=list");
        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", "Invalid role. Allowed values are Contributor, Editor.");
            listAuthors(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", "An unexpected error occurred. Please try again.");
            listAuthors(request, response);
        }
    }

    private void setAuthorRole(HttpServletRequest request, Author author) {
        try {
            AuthorRole role = AuthorRole.valueOf(request.getParameter("role").toUpperCase());
            author.setRole(role);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role. Allowed values are Contributor, Editor.");
        }
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) {
        try {
            Long authorId = Long.parseLong(request.getParameter("id"));
            Author author = authorService.getAuthorById(authorId);

            if (author != null) {
                request.setAttribute("author", author);
                request.setAttribute("authorRoles", AuthorRole.values()); // Ensure this is set
                request.getRequestDispatcher("/WEB-INF/views/author/edit.jsp").forward(request, response);
            } else {
                request.setAttribute("errorMessage", "Author not found.");
                listAuthors(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid author ID.");
            listAuthors(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", "An unexpected error occurred.");
            listAuthors(request, response);
        }
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.getRequestDispatcher("/WEB-INF/views/author/addAuthor.jsp").forward(request, response);
        } catch (ServletException | IOException e) {
            throw new RuntimeException("Error while showing add author form.", e);
        }
    }
}