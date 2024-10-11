<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Blog Application</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
    <style>
        body { background-color: #f8f9fa; }
        .card { transition: transform 0.3s; }
        .card:hover { transform: translateY(-5px); }
    </style>
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <a class="navbar-brand" href="${pageContext.request.contextPath}/">Blog Application</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav ml-auto">
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/article/list">All Articles</a>
            </li>
            <c:if test="${not empty sessionScope.loggedInUser}">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/article/create">Create Article</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/logout">Logout</a>
                </li>
            </c:if>
            <c:if test="${empty sessionScope.loggedInUser}">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/login">Login</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/register">Register</a>
                </li>
            </c:if>
        </ul>
    </div>
</nav>

<div class="container mt-5">
    <h1 class="mb-4">Welcome to Our Blog</h1>

    <form action="${pageContext.request.contextPath}/search" method="get" class="mb-4">
        <div class="input-group">
            <input type="text" class="form-control" name="query" placeholder="Search articles..." value="${query}">
            <div class="input-group-append">
                <button class="btn btn-outline-secondary" type="submit">Search</button>
            </div>
        </div>
    </form>

    <c:if test="${not empty articles}">
        <div class="row">
            <c:forEach var="article" items="${articles}">
                <div class="col-md-4 mb-4">
                    <div class="card h-100">
                        <div class="card-body">
                            <h5 class="card-title">${article.title}</h5>
                            <h6 class="card-subtitle mb-2 text-muted">By ${article.author.name}</h6>
                            <p class="card-text">${fn:substring(article.content, 0, 100)}...</p>
                        </div>
                        <div class="card-footer">
                            <small class="text-muted">Created on: ${article.creationDate}</small>
                            <span class="badge badge-secondary float-right mr-2">${fn:length(article.comments)} comments</span>
                            <a href="${pageContext.request.contextPath}/article/view?id=${article.id}" class="btn btn-primary float-right">Read More</a>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>

        <nav aria-label="Page navigation" class="mt-4">
            <ul class="pagination justify-content-center">
                <c:forEach begin="1" end="${noOfPages}" var="i">
                    <li class="page-item ${currentPage eq i ? 'active' : ''}">
                        <a class="page-link" href="${pageContext.request.contextPath}/search?page=${i}&query=${query}">${i}</a>
                    </li>
                </c:forEach>
            </ul>
        </nav>
    </c:if>

    <c:if test="${empty articles}">
        <p>No articles found.</p>
    </c:if>
</div>

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.3/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>