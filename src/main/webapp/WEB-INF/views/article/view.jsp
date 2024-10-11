<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
  // Check if the session attribute "loggedInUser" is set
  if (session.getAttribute("loggedInUser") == null) {
    response.sendRedirect("login.jsp");
    return; // Ensure no further processing happens after redirect
  }
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>${article.title}</title>
  <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
  <style>
    .article-content {
      white-space: pre-wrap;
    }
  </style>
</head>
<body class="bg-gray-900">

<div class="content flex">
  <!-- Sidebar -->
  <%@ include file="/dashboard.jsp" %>

  <!-- Main Content -->
  <div class="flex-1 p-6 main-content rounded-lg">
    <h1 class="mb-4 text-2xl font-bold text-white">${article.title}</h1>
    <div class="bg-white shadow-md rounded-lg p-6 mb-6">
      <h6 class="text-gray-600 mb-2">By ${article.author.name}</h6>
      <p class="text-gray-500 mb-2"><small>Created on: ${article.creationDate}</small></p>
      <p class="text-gray-500 mb-4"><small>Status: ${article.status}</small></p>
      <hr />
      <div class="article-content mt-4">${article.content}</div>
    </div>
    <div class="flex space-x-2">
      <a href="<c:url value='/article/edit?id=${article.id}'/>" class="btn bg-yellow-500 hover:bg-yellow-600 text-white py-2 px-4 rounded">
        <i class="fas fa-edit"></i> Edit
      </a>
      <a href="<c:url value='/article/delete?id=${article.id}'/>" class="btn bg-red-500 hover:bg-red-600 text-white py-2 px-4 rounded"
         onclick="return confirm('Are you sure you want to delete this article?')">
        <i class="fas fa-trash-alt"></i> Delete
      </a>
      <a href="<c:url value='/article/list'/>" class="btn bg-gray-500 hover:bg-gray-600 text-white py-2 px-4 rounded">
        <i class="fas fa-arrow-left"></i> Back to List
      </a>
    </div>
    <div class="mt-5">
      <h2 class="text-xl font-semibold text-white">Comments</h2>
      <c:if test="${sessionScope.userRole eq 'Editor'}">
        <a href="<c:url value='/comment/manage'/>" class="btn bg-blue-500 hover:bg-blue-600 text-white mb-3">Manage Comments</a>
      </c:if>
      <form action="${pageContext.request.contextPath}/comment/create" method="post" id="addCommentForm" class="mb-5">
        <input type="hidden" name="articleId" value="${article.id}">
        <div class="form-group">
          <label for="content" class="block text-gray-700">Add a comment:</label>
          <textarea class="form-control border rounded w-full p-2" id="content" name="content" rows="3" required></textarea>
        </div>
        <button type="submit" class="btn bg-green-500 hover:bg-green-600 text-white py-2 px-4 rounded">Submit Comment</button>
      </form>
      <c:forEach var="comment" items="${article.comments}">
        <div class="bg-gray-50 shadow-md rounded-lg p-4 mt-3">
          <h6 class="text-gray-600">By ${comment.author.name}</h6>
          <p>${comment.content}</p>
          <p class="text-gray-500"><small>Status: ${comment.status}</small></p>
          <c:if test="${sessionScope.userRole eq 'ADMIN'}">
            <form action="${pageContext.request.contextPath}/comment/updateStatus" method="post" style="display: inline;">
              <input type="hidden" name="id" value="${comment.id}">
              <input type="hidden" name="articleId" value="${article.id}">
              <c:if test="${comment.status ne 'approved'}">
                <button type="submit" name="status" value="approved" class="btn btn-sm bg-green-500 hover:bg-green-600 text-white">Approve</button>
              </c:if>
              <c:if test="${comment.status ne 'rejected'}">
                <button type="submit" name="status" value="rejected" class="btn btn-sm bg-red-500 hover:bg-red-600 text-white">Reject</button>
              </c:if>
            </form>
          </c:if>
        </div>
      </c:forEach>
    </div>
  </div>
</div>

</body>
</html>
