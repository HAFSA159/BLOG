<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Search Results</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
</head>
<body>
    <div class="container mt-5">
        <h1 class="mb-4">Search Results for "${query}"</h1>
        
        <c:if test="${empty searchResults}">
            <p>No results found for your search query.</p>
        </c:if>
        
        <c:forEach var="article" items="${searchResults}">
            <div class="card mb-3">
                <div class="card-body">
                    <h5 class="card-title">${article.title}</h5>
                    <h6 class="card-subtitle mb-2 text-muted">By ${article.author.name}</h6>
                    <p class="card-text">${article.content.substring(0, Math.min(article.content.length(), 200))}...</p>
                    <a href="<c:url value='/article/view?id=${article.id}'/>" class="btn btn-primary">Read More</a>
                </div>
            </div>
        </c:forEach>
        
        <c:if test="${noOfPages > 1}">
            <nav aria-label="Search results pages">
                <ul class="pagination justify-content-center">
                    <c:forEach begin="1" end="${noOfPages}" var="i">
                        <li class="page-item ${currentPage eq i ? 'active' : ''}">
                            <a class="page-link" href="<c:url value='/search?query=${query}&page=${i}'/>">${i}</a>
                        </li>
                    </c:forEach>
                </ul>
            </nav>
        </c:if>
        
        <a href="<c:url value='/'/>" class="btn btn-secondary mt-3"><i class="fas fa-arrow-left"></i> Back to Home</a>
    </div>

    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.3/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>