<%--
  Created by IntelliJ IDEA.
  User: ssngn
  Date: 07/10/2024
  Time: 17:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${param.title} - Blog Application</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<c:url value='/css/styles.css'/>">
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <div class="container">
            <a class="navbar-brand" href="<c:url value='/'/>">Blog App</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav">
                    <li class="nav-item">
                        <a class="nav-link" href="<c:url value='/article'/>">Articles</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="<c:url value='/author'/>">Authors</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="<c:url value='/comment'/>">Comments</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>
    
    <main class="container mt-4">
        <h1>${param.title}</h1>
        <jsp:include page="${param.content}" />
    </main>
    
    <footer class="bg-light text-center text-lg-start mt-4">
        <div class="text-center p-3" style="background-color: rgba(0, 0, 0, 0.2);">
            © 2023 Blog Application
        </div>
    </footer>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
    <script src="<c:url value='/js/script.js'/>"></script>
</body>
</html>
