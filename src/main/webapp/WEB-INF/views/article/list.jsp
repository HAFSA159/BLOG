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
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Article List</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
    <style>
        .card-hover:hover {
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
            transition: box-shadow 0.3s ease-in-out;
        }
    </style>
</head>
<body>
    <div class="container mt-5">
        <h1 class="mb-4 text-center">Article List</h1>
        <div class="row mb-3">
            <div class="col-md-6">
                <a href="<c:url value='/article/create'/>" class="btn btn-primary"><i class="fas fa-plus"></i> Create New Article</a>
            </div>
            <div class="col-md-6">
                <form action="<c:url value='/article/list'/>" method="get" class="form-inline justify-content-end">
                    <input type="text" name="searchTitle" class="form-control mr-2" placeholder="Search by title" value="${searchTitle}">
                    <button type="submit" class="btn btn-outline-secondary"><i class="fas fa-search"></i> Search</button>
                </form>
            </div>
        </div>
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>Title</th>
                    <th>Author</th>
                    <th>Content Preview</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="article" items="${articles}">
                    <tr>
                        <td>${article.title}</td>
                        <td>${article.author.name}</td>
                        <td>
                            ${article.content != null ? (article.content.length() > 100 ? article.content.substring(0, 100) : article.content) : ''}
                            ${article.content != null && article.content.length() > 100 ? '...' : ''}
                        </td>
                        <td>
                            <a href="<c:url value='/article/view?id=${article.id}'/>" class="btn btn-sm btn-info"><i class="fas fa-eye"></i> View</a>
                            <a href="<c:url value='/article/edit?id=${article.id}'/>" class="btn btn-sm btn-warning"><i class="fas fa-edit"></i> Edit</a>
                            <a href="<c:url value='/article/delete?id=${article.id}'/>" class="btn btn-sm btn-danger"><i class="fas fa-trash-alt"></i> Delete</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <nav aria-label="Page navigation">
            <ul class="pagination justify-content-center">
                <c:forEach begin="1" end="${noOfPages}" var="i">
                    <li class="page-item ${currentPage eq i ? 'active' : ''}">
                        <a class="page-link" href="<c:url value='/article/list?page=${i}&searchTitle=${searchTitle}'/>">${i}</a>
                    </li>
                </c:forEach>
            </ul>
        </nav>
    </div>
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.3/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>