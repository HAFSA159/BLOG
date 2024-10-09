package com.blogapp.controller;

import com.blogapp.repository.impl.AuthorRepositoryImpl;
import com.blogapp.service.AuthorService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {

    private final AuthorService authorService;

    public LoginServlet() {
        // Initialize the AuthorService with an instance of AuthorRepositoryImpl
        this.authorService = new AuthorService(new AuthorRepositoryImpl());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Authenticate the user using the AuthorService
        if (authorService.authenticate(email, password)) {
            HttpSession session = request.getSession();
            session.setAttribute("loggedInUser", email);
            response.sendRedirect("index.jsp"); // Redirect to index.jsp upon successful login
        } else {
            request.setAttribute("error", "Invalid email or password");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
