<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${article.title}</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
</head>
<body class="bg-gray-100">
    <nav class="bg-white shadow-lg">
        <div class="max-w-6xl mx-auto px-4">
            <div class="flex justify-between">
                <div class="flex space-x-7">
                    <a href="${pageContext.request.contextPath}/" class="flex items-center py-4 px-2">
                        <span class="font-semibold text-gray-500 text-lg">Blog App</span>
                    </a>
                </div>
                <div class="flex items-center space-x-3">
                    <a href="${pageContext.request.contextPath}/home" class="py-2 px-2 font-medium text-gray-500 rounded hover:bg-green-500 hover:text-white transition duration-300">Home</a>
                    <c:choose>
                        <c:when test="${empty sessionScope.loggedInUser}">
                            <a href="${pageContext.request.contextPath}/login.jsp" class="py-2 px-2 font-medium text-gray-500 rounded hover:bg-green-500 hover:text-white transition duration-300">Log In</a>
                            <a href="${pageContext.request.contextPath}/register.jsp" class="py-2 px-2 font-medium text-white bg-green-500 rounded hover:bg-green-400 transition duration-300">Sign Up</a>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/index.jsp" class="py-2 px-2 font-medium text-gray-500 rounded hover:bg-green-500 hover:text-white transition duration-300">Dashboard</a>
                            <a href="${pageContext.request.contextPath}/logout" class="py-2 px-2 font-medium text-white bg-red-500 rounded hover:bg-red-400 transition duration-300">Logout</a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </nav>

    <main class="container mx-auto mt-8 px-4">
        <article class="bg-white shadow-lg rounded-lg overflow-hidden">
            <div class="p-6">
                <h1 class="text-3xl font-bold text-gray-800 mb-4">${article.title}</h1>
                <div class="flex items-center text-gray-600 mb-4">
                    <span class="mr-4"><i class="fas fa-user mr-2"></i>${article.author.name}</span>
                    <span class="mr-4"><i class="far fa-calendar-alt mr-2"></i>${article.creationDate}</span>
                    <span><i class="fas fa-tag mr-2"></i>${article.status}</span>
                </div>
                <div class="prose max-w-none">
                    ${article.content}
                </div>
            </div>
        </article>

        <section class="mt-8">
            <h2 class="text-2xl font-bold text-gray-800 mb-4">Comments</h2>
            <c:forEach var="comment" items="${article.comments}">
                <c:if test="${comment.status eq 'approved'}">
                    <div class="bg-gray-50 shadow rounded-lg p-4 mb-4">
                        <div class="flex justify-between items-start">
                            <div>
                                <h3 class="font-semibold text-gray-800">${comment.author.name}</h3>
                                <p class="mt-2 text-gray-600">${comment.content}</p>
                            </div>
                        </div>
                        <c:if test="${comment.author.email eq sessionScope.loggedInUser || sessionScope.userRole eq 'Editor'}">
                            <div class="mt-4 flex space-x-2">
                                <c:if test="${comment.author.email eq sessionScope.loggedInUser}">
                                    <button onclick="openEditPopup(${comment.id}, '${comment.content}')" class="text-blue-500 hover:text-blue-600">
                                        <i class="fas fa-edit mr-1"></i>Edit
                                    </button>
                                    <form action="${pageContext.request.contextPath}/comment/delete" method="post" class="inline">
                                        <input type="hidden" name="commentId" value="${comment.id}">
                                        <button type="submit" class="text-red-500 hover:text-red-600">
                                            <i class="fas fa-trash-alt mr-1"></i>Delete
                                        </button>
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
                        </c:if>
                    </div>
                </c:if>
            </c:forEach>

            <c:if test="${not empty sessionScope.loggedInUser}">
                <form action="${pageContext.request.contextPath}/comment/create" method="post" id="addCommentForm" class="mt-8">
                    <input type="hidden" name="articleId" value="${article.id}">
                    <div class="mb-4">
                        <label for="content" class="block text-gray-700 font-bold mb-2">Add a comment:</label>
                        <textarea class="w-full px-3 py-2 text-gray-700 border rounded-lg focus:outline-none" rows="4" id="content" name="content" required></textarea>
                    </div>
                    <button type="submit" class="bg-blue-500 hover:bg-blue-600 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">
                        Submit Comment
                    </button>
                </form>
            </c:if>
            <c:if test="${empty sessionScope.loggedInUser}">
                <p class="mt-8 text-gray-600">Please <a href="${pageContext.request.contextPath}/login" class="text-blue-500 hover:text-blue-700">log in</a> to leave a comment.</p>
            </c:if>
        </section>
    </main>

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