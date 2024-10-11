<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
  // Check if the session attributes "loggedInUser" and "userRole" are set
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
      <c:forEach var="comment" items="${article.comments}">
        <div class="bg-gray-50 shadow-md rounded-lg p-4 mt-3">
          <h6 class="text-gray-600">By ${comment.author.name}</h6>
          <p>${comment.content}</p>
          <p class="text-gray-500"><small>Status: ${comment.status}</small></p>
          <c:if test="${comment.author.email eq sessionScope.loggedInUser}">
            <button class="bg-blue-500 hover:bg-blue-600 text-white py-1 px-2 rounded text-sm" onclick="openEditPopup(${comment.id}, '${comment.content}')">Edit</button>
            <form action="${pageContext.request.contextPath}/comment/delete" method="post" class="inline">
              <input type="hidden" name="commentId" value="${comment.id}">
              <button type="submit" class="bg-red-500 hover:bg-red-600 text-white py-1 px-2 rounded text-sm">Delete</button>
            </form>
          </c:if>
          <c:if test="${sessionScope.userRole eq 'Editor'}">
            <form action="${pageContext.request.contextPath}/comment/updateStatus" method="post" class="inline">
              <input type="hidden" name="commentId" value="${comment.id}">
              <select name="status" onchange="this.form.submit()" class="bg-gray-200 rounded px-2 py-1 text-sm">
                <option value="approved" ${comment.status eq 'approved' ? 'selected' : ''}>Approved</option>
                <option value="rejected" ${comment.status eq 'rejected' ? 'selected' : ''}>Rejected</option>
              </select>
            </form>
          </c:if>
        </div>
      </c:forEach>
      <form action="${pageContext.request.contextPath}/comment/create" method="post" id="addCommentForm" class="mt-4">
        <input type="hidden" name="articleId" value="${article.id}">
        <div class="mb-4">
          <label for="content" class="block text-white mb-2">Add a comment:</label>
          <textarea class="w-full p-2 border rounded" id="content" name="content" rows="3" required></textarea>
        </div>
        <button type="submit" class="bg-green-500 hover:bg-green-600 text-white py-2 px-4 rounded">Submit Comment</button>
      </form>
    </div>
  </div>
</div>

<div id="editPopup" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full hidden">
  <div class="relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white">
    <h3 class="text-lg font-medium leading-6 text-gray-900 mb-2">Edit Comment</h3>
    <textarea id="editCommentContent" class="w-full p-2 border rounded mb-4" rows="3"></textarea>
    <div class="flex justify-end">
      <button onclick="closeEditPopup()" class="bg-gray-500 hover:bg-gray-600 text-white py-2 px-4 rounded mr-2">Cancel</button>
      <button onclick="submitEditComment()" class="bg-blue-500 hover:bg-blue-600 text-white py-2 px-4 rounded">Save</button>
    </div>
  </div>
</div>

<script>
  let currentCommentId;

  function openEditPopup(commentId, content) {
    currentCommentId = commentId;
    document.getElementById('editCommentContent').value = content;
    document.getElementById('editPopup').classList.remove('hidden');
  }

  function closeEditPopup() {
    document.getElementById('editPopup').classList.add('hidden');
  }

  function submitEditComment() {
    const newContent = document.getElementById('editCommentContent').value;
    if (newContent.trim() !== "") {
      const form = document.createElement("form");
      form.method = "post";
      form.action = "${pageContext.request.contextPath}/comment/edit";
      
      const commentIdInput = document.createElement("input");
      commentIdInput.type = "hidden";
      commentIdInput.name = "commentId";
      commentIdInput.value = currentCommentId;
      form.appendChild(commentIdInput);

      const contentInput = document.createElement("input");
      contentInput.type = "hidden";
      contentInput.name = "content";
      contentInput.value = newContent;
      form.appendChild(contentInput);

      document.body.appendChild(form);
      form.submit();
    }
  }
</script>

</body>
</html>