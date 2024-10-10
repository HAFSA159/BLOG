<%
    // Set response headers to prevent caching
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
    response.setHeader("Pragma", "no-cache"); // HTTP 1.0
    response.setDateHeader("Expires", 0); // Proxies
%>
<%
    // Check if the session attribute "loggedInUser" is set
    if (session.getAttribute("loggedInUser") == null) {
        response.sendRedirect("login.jsp");
        return; // Ensure no further processing happens after redirect
    }
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Blog Application - Home</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-5">
    <h1 class="mb-4">Welcome to Our Blog</h1>
    <p>Explore our latest articles or create your own!</p>
    <div class="mt-4">
        <a href="<c:url value='/article/list'/>" class="btn btn-primary mr-2">View Articles</a>
        <a href="<c:url value='/article/create'/>" class="btn btn-success">Create Article</a>
        <a href="<c:url value='/author/list'/>" class="btn btn-info">View Authors</a> <!-- Link to Author List -->
        <a href="<c:url value='/logout'/>" class="btn btn-danger">Log Out</a> <!-- Logout Button -->
    </div>
</div>
</body>
</html>
