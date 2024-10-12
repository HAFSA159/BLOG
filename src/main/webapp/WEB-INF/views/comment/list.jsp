<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Comments</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
    <link rel="stylesheet" href="https://cdn.datatables.net/1.13.6/css/jquery.dataTables.min.css">
    <style>
        body {
            display: flex;
            height: 100vh; /* Full height of viewport */
            overflow: hidden; /* Prevent body overflow */
        }
        .sidebar {
            background-color: rgba(44, 62, 80, 0.85);
            backdrop-filter: blur(5px);
            width: 250px; /* Fixed width for the sidebar */
            padding: 20px; /* Padding for aesthetics */
            overflow-y: auto; /* Scroll if needed */
            position: relative; /* Positioned relative to parent */
        }
        .main-content {
            background-color: rgba(255, 255, 255, 0.85);
            backdrop-filter: blur(5px);
            flex: 1; /* Take remaining space */
            padding: 20px;
            overflow-y: auto; /* Add scroll if needed */
            margin-left: 20px; /* Margin to separate from sidebar */
        }
        /* Make the table responsive */
        table {
            width: 100%; /* Full width */
            border-collapse: collapse; /* Remove space between cells */
            margin-top: 20px; /* Margin to separate table from header */
        }
        th, td {
            padding: 12px; /* Padding inside cells */
            text-align: left; /* Align text to the left */
        }
        th {
            background-color: #f0f0f0; /* Light gray background for header */
        }
        tr:nth-child(even) {
            background-color: #f9f9f9; /* Zebra striping for rows */
        }
        .actions {
            display: flex; /* Use flexbox for actions */
            gap: 5px; /* Space between action buttons */
        }
    </style>
</head>
<body class="bg-gray-100">
<div class="content flex">
    <%@ include file="/dashboard.jsp" %> <!-- Include the dashboard sidebar here -->


<div class="main-content">
    <h1 class="text-3xl font-bold mb-6">Manage Comments</h1>

    <div class="bg-white shadow-md rounded-lg overflow-hidden">
        <table id="commentsTable" class="min-w-full text-sm text-left text-gray-800">
            <thead>
            <tr>
                <th>Content</th>
                <th>Author</th>
                <th>Article</th>
                <th>Status</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody class="divide-y divide-gray-200">
            <c:forEach var="comment" items="${comments}">
                <tr>
                    <td>${comment.content}</td>
                    <td>${comment.author.name}</td>
                    <td>${comment.article.title}</td>
                    <td>${comment.status}</td>
                    <td>
                        <div class="actions">
                            <c:choose>
                                <c:when test="${sessionScope.userRole eq 'Editor' and comment.author.email ne sessionScope.loggedInUser}">
                                    <form action="${pageContext.request.contextPath}/comment/updateStatus" method="post" class="inline">
                                        <input type="hidden" name="commentId" value="${comment.id}">
                                        <select name="status" onchange="this.form.submit()" class="bg-gray-200 rounded px-2 py-1 text-sm">
                                            <option value="approved" ${comment.status eq 'approved' ? 'selected' : ''}>Approved</option>
                                            <option value="rejected" ${comment.status eq 'rejected' ? 'selected' : ''}>Rejected</option>
                                        </select>
                                    </form>
                                </c:when>
                                <c:when test="${sessionScope.userRole eq 'Editor' and comment.author.email eq sessionScope.loggedInUser}">
                                    <button class="bg-blue-500 hover:bg-blue-600 text-white py-1 px-2 rounded text-sm" onclick="openEditPopup(${comment.id}, '${comment.content}')">Edit</button>
                                    <form action="${pageContext.request.contextPath}/comment/delete" method="post" class="inline">
                                        <input type="hidden" name="commentId" value="${comment.id}">
                                        <button type="submit" class="bg-red-500 hover:bg-red-600 text-white py-1 px-2 rounded text-sm">Delete</button>
                                    </form>
                                    <form action="${pageContext.request.contextPath}/comment/updateStatus" method="post" class="inline">
                                        <input type="hidden" name="commentId" value="${comment.id}">
                                        <select name="status" onchange="this.form.submit()" class="bg-gray-200 rounded px-2 py-1 text-sm">
                                            <option value="approved" ${comment.status eq 'approved' ? 'selected' : ''}>Approved</option>
                                            <option value="rejected" ${comment.status eq 'rejected' ? 'selected' : ''}>Rejected</option>
                                        </select>
                                    </form>
                                </c:when>
                                <c:when test="${comment.author.email eq sessionScope.loggedInUser}">
                                    <button class="bg-blue-500 hover:bg-blue-600 text-white py-1 px-2 rounded text-sm" onclick="openEditPopup(${comment.id}, '${comment.content}')">Edit</button>
                                    <form action="${pageContext.request.contextPath}/comment/delete" method="post" class="inline">
                                        <input type="hidden" name="commentId" value="${comment.id}">
                                        <button type="submit" class="bg-red-500 hover:bg-red-600 text-white py-1 px-2 rounded text-sm">Delete</button>
                                    </form>
                                </c:when>
                            </c:choose>
                        </div>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
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

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
<script>
    $(document).ready(function() {
        $('#commentsTable').DataTable(); // Initialize DataTable
    });

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
        closeEditPopup();
    }
</script>
</body>
</html>
