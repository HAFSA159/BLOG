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
    </div>
</div>
</body>
</html>
