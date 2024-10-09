<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Comments</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
</head>
<body>
    <div class="container mt-5">
        <h1 class="mb-4">Manage Comments</h1>
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>Article</th>
                    <th>Author</th>
                    <th>Content</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="comment" items="${comments}">
                    <tr>
                        <td>${comment.article.title}</td>
                        <td>${comment.author.name}</td>
                        <td>${comment.content}</td>
                        <td>${comment.status}</td>
                        <td>
                            <form action="${pageContext.request.contextPath}/comment/updateStatus" method="post" style="display: inline;">
                                <input type="hidden" name="id" value="${comment.id}">
                                <c:if test="${comment.status ne 'approved'}">
                                    <button type="submit" name="status" value="approved" class="btn btn-sm btn-success">Approve</button>
                                </c:if>
                                <c:if test="${comment.status ne 'rejected'}">
                                    <button type="submit" name="status" value="rejected" class="btn btn-sm btn-danger">Reject</button>
                                </c:if>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</body>
</html>