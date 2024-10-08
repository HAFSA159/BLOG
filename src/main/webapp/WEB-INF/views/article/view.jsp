<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${article.title}</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
    <style>
        .article-content {
            white-space: pre-wrap;
        }
    </style>
</head>
<body>
    <div class="container mt-5">
        <h1 class="mb-4">${article.title}</h1>
        <div class="card">
            <div class="card-body">
                <h6 class="card-subtitle mb-2 text-muted">By ${article.author.name}</h6>
                <p class="card-text"><small class="text-muted">Created on: ${article.creationDate}</small></p>
                <p class="card-text"><small class="text-muted">Status: ${article.status}</small></p>
                <hr>
                <div class="article-content">
                    ${article.content}
                </div>
            </div>
            <div class="card-footer bg-transparent">
                <a href="<c:url value='/article/edit?id=${article.id}'/>" class="btn btn-warning"><i class="fas fa-edit"></i> Edit</a>
                <a href="<c:url value='/article/delete?id=${article.id}'/>" class="btn btn-danger" onclick="return confirm('Are you sure you want to delete this article?')"><i class="fas fa-trash-alt"></i> Delete</a>
                <a href="<c:url value='/article/list'/>" class="btn btn-secondary"><i class="fas fa-arrow-left"></i> Back to List</a>
            </div>
        </div>
    </div>
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.3/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
