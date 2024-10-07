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
        <h2>Author List</h2>
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Birth Date</th>
                    <th>Role</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="author" items="${authors}">
                    <tr>
                        <td>${author.name}</td>
                        <td>${author.email}</td>
                        <td><fmt:formatDate value="${author.birthdate}" pattern="yyyy-MM-dd" /></td>
                        <td>${author.role}</td>
                        <td>
                            <a href="<c:url value='/author?action=edit&id=${author.id}'/>" class="btn btn-sm btn-primary">Edit</a>
                            <a href="<c:url value='/author?action=delete&id=${author.id}'/>" class="btn btn-sm btn-danger" onclick="return confirm('Are you sure you want to delete this author?')">Delete</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
    <div class="col-md-4">
        <h2>Add New Author</h2>
        <form action="<c:url value='/author'/>" method="post">
            <input type="hidden" name="action" value="add">
            <div class="mb-3">
                <label for="name" class="form-label">Name</label>
                <input type="text" class="form-control" id="name" name="name" required>
            </div>
            <div class="mb-3">
                <label for="email" class="form-label">Email</label>
                <input type="email" class="form-control" id="email" name="email" required>
            </div>
            <div class="mb-3">
                <label for="birthdate" class="form-label">Birth Date</label>
                <input type="date" class="form-control" id="birthdate" name="birthdate" required>
            </div>
            <div class="mb-3">
                <label for="role" class="form-label">Role</label>
                <select class="form-select" id="role" name="role" required>
                    <option value="admin">Admin</option>
                    <option value="author">Author</option>
                </select>
            </div>
            <button type="submit" class="btn btn-primary">Add Author</button>
        </form>
    </div>
</div>
