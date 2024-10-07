package com.blogapp.controller;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet(name = "ArticleServlet", value = "/article")
public class ArticleServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(ArticleServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.debug("ArticleServlet doGet method called");
        
        request.setAttribute("title", "Articles");
        request.getRequestDispatcher("/WEB-INF/views/articles.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.debug("ArticleServlet doPost method called");
    }
}