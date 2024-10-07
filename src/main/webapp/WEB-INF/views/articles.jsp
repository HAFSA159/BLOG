<%--
  Created by IntelliJ IDEA.
  User: ssngn
  Date: 07/10/2024
  Time: 17:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div class="row">
    <div class="col-md-8">
        <h2>Article List</h2>
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>Title</th>
                    <th>Author</th>
                    <th>Creation Date</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="article" items="${articles}">
                    <tr>
                        <td>${article.title}</td>
                        <td>${article.author.name}</td>
                        <td><fmt:formatDate value="${article.creationDate}" pattern="yyyy-MM-dd HH:mm" /></td>
                        <td>${article.status}</td>
                        <td>
                            <a href="<c:url value='/article?action=edit&id=${article.id}'/>" class="btn btn-sm btn-primary">Edit</a>
                            <a href="<c:url value='/article?action=delete&id=${article.id}'/>" class="btn btn-sm btn-danger" onclick="return confirm('Are you sure you want to delete this article?')">Delete</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
    <div class="col-md-4">
        <h2>Add New Article</h2>
        <form action="<c:url value='/article'/>" method="post">
            <input type="hidden" name="action" value="add">
            <div class="mb-3">
                <label for="title" class="form-label">Title</label>
                <input type="text" class="form-control" id="title" name="title" required>
            </div>
            <div class="mb-3">
                <label for="content" class="form-label">Content</label>
                <textarea class="form-control" id="content" name="content" rows="5" required></textarea>
            </div>
            <div class="mb-3">
                <label for="author" class="form-label">Author</label>
                <select class="form-select" id="author" name="authorId" required>
                    <c:forEach var="author" items="${authors}">
                        <option value="${author.id}">${author.name}</option>
                    </c:forEach>
                </select>
            </div>
            <button type="submit" class="btn btn-primary">Add Article</button>
        </form>
    </div>
</div>
