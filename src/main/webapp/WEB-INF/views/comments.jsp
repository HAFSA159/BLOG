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
        <h2>Comment List</h2>
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>Content</th>
                    <th>Creation Date</th>
                    <th>Status</th>
                    <th>Article</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="comment" items="${comments}">
                    <tr>
                        <td>${comment.content}</td>
                        <td><fmt:formatDate value="${comment.creationDate}" pattern="yyyy-MM-dd HH:mm" /></td>
                        <td>${comment.status}</td>
                        <td>${comment.article.title}</td>
                        <td>
                            <a href="<c:url value='/comment?action=edit&id=${comment.id}'/>" class="btn btn-sm btn-primary">Edit</a>
                            <a href="<c:url value='/comment?action=delete&id=${comment.id}'/>" class="btn btn-sm btn-danger" onclick="return confirm('Are you sure you want to delete this comment?')">Delete</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
    <div class="col-md-4">
        <h2>Add New Comment</h2>
        <form action="<c:url value='/comment'/>" method="post">
            <input type="hidden" name="action" value="add">
            <div class="mb-3">
                <label for="content" class="form-label">Content</label>
                <textarea class="form-control" id="content" name="content" rows="3" required></textarea>
            </div>
            <div class="mb-3">
                <label for="article" class="form-label">Article</label>
                <select class="form-select" id="article" name="articleId" required>
                    <c:forEach var="article" items="${articles}">
                        <option value="${article.id}">${article.title}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="mb-3">
                <label for="status" class="form-label">Status</label>
                <select class="form-select" id="status" name="status" required>
                    <option value="approved">Approved</option>
                    <option value="rejected">Rejected</option>
                </select>
            </div>
            <button type="submit" class="btn btn-primary">Add Comment</button>
        </form>
    </div>
</div>
