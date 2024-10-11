<%
  // Check if the session attributes "loggedInUser" and "userRole" are set
  if (session.getAttribute("loggedInUser") == null) {
    response.sendRedirect("login.jsp");
    return; // Ensure no further processing happens after redirect
  }
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>${article.title}</title>
    <link
      rel="stylesheet"
      href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"
    />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"
    />
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
          <h6 class="card-subtitle mb-2 text-muted">
            By ${article.author.name}
          </h6>
          <p class="card-text">
            <small class="text-muted"
              >Created on: ${article.creationDate}</small
            >
          </p>
          <p class="card-text">
            <small class="text-muted">Status: ${article.status}</small>
          </p>
          <hr />
          <div class="article-content">${article.content}</div>
        </div>
        <div class="card-footer bg-transparent">
          <a
            href="<c:url value='/article/edit?id=${article.id}'/>"
            class="btn btn-warning"
            ><i class="fas fa-edit"></i> Edit</a
          >
          <a
            href="<c:url value='/article/delete?id=${article.id}'/>"
            class="btn btn-danger"
            onclick="return confirm('Are you sure you want to delete this article?')"
            ><i class="fas fa-trash-alt"></i> Delete</a
          >
          <a href="<c:url value='/article/list'/>" class="btn btn-secondary"
            ><i class="fas fa-arrow-left"></i> Back to List</a
          >
        </div>
      </div>
      <h3 class="mt-4">Comments</h3>
      <c:forEach var="comment" items="${article.comments}">
        <div class="card mt-3">
          <div class="card-body">
            <h6 class="card-subtitle mb-2 text-muted">By ${comment.author.name}</h6>
            <p class="card-text">${comment.content}</p>
            <p class="card-text"><small class="text-muted">Status: ${comment.status}</small></p>
            <c:if test="${comment.author.email eq sessionScope.loggedInUser}">
              <button class="btn btn-sm btn-primary" onclick="editComment(${comment.id}, '${comment.content}')">Edit</button>
              <form action="${pageContext.request.contextPath}/comment/delete" method="post" style="display: inline;">
                <input type="hidden" name="commentId" value="${comment.id}">
                <button type="submit" class="btn btn-sm btn-danger">Delete</button>
              </form>
            </c:if>
            <c:if test="${sessionScope.userRole eq 'EDITOR' or sessionScope.userRole eq 'ADMIN'}">
              <form action="${pageContext.request.contextPath}/comment/updateStatus" method="post" style="display: inline;">
                <input type="hidden" name="commentId" value="${comment.id}">
                <select name="status" onchange="this.form.submit()">
                  <option value="approved" ${comment.status eq 'approved' ? 'selected' : ''}>Approved</option>
                  <option value="rejected" ${comment.status eq 'rejected' ? 'selected' : ''}>Rejected</option>
                </select>
              </form>
            </c:if>
          </div>
        </div>
      </c:forEach>
      <form action="${pageContext.request.contextPath}/comment/create" method="post" id="addCommentForm" class="mt-4">
        <input type="hidden" name="articleId" value="${article.id}">
        <div class="form-group">
          <label for="content">Add a comment:</label>
          <textarea class="form-control" id="content" name="content" rows="3" required></textarea>
        </div>
        <button type="submit" class="btn btn-primary">Submit Comment</button>
      </form>

      <script>
        function editComment(commentId, content) {
          const newContent = prompt("Edit your comment:", content);
          if (newContent !== null && newContent !== "") {
            const form = document.createElement("form");
            form.method = "post";
            form.action = "${pageContext.request.contextPath}/comment/edit";
            
            const commentIdInput = document.createElement("input");
            commentIdInput.type = "hidden";
            commentIdInput.name = "commentId";
            commentIdInput.value = commentId;
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
    </div>
  </body>
</html>