<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.blogapp.model.Article" %>
<%@ page import="com.blogapp.model.Author" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.datatables.net/1.13.6/css/jquery.dataTables.min.css">
    <title>Articles List</title>
    <style>
        .blur-bg {
            background-image: url('/images/back.jpg');
            background-size: cover;
            background-position: center;
            filter: blur(10px);
            -webkit-filter: blur(10px);
            position: fixed;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            z-index: 0;
        }
        .content {
            position: relative;
            z-index: 10;
        }
        .sidebar {
            background-color: rgba(44, 62, 80, 0.85);
            backdrop-filter: blur(5px);
        }
        .main-content {
            background-color: rgba(255, 255, 255, 0.85);
            backdrop-filter: blur(5px);
        }
        .table-container {
            background: rgba(255, 255, 255, 0.85);
            backdrop-filter: blur(10px);
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
        }
        .btn-primary {
            background-color: #2563eb;
            color: #fff;
            transition: background-color 0.3s ease;
        }
        .btn-primary:hover {
            background-color: #1d4ed8;
        }
    </style>
</head>
<body class="bg-gray-900">
<!-- Background blur -->
<div class="blur-bg"></div>

<!-- Main Content with Sidebar -->
<div class="content flex">
    <!-- Sidebar -->
    <%@ include file="/dashboard.jsp" %>
    <!-- Articles Table Content -->
    <div class="flex-1 p-6 main-content rounded-lg">
        <div class="table-container overflow-x-auto p-4">
            <table id="articleTable" class="w-full text-sm text-left text-gray-800">
                <caption class="p-5 text-lg font-semibold text-gray-900">
                    Articles List
                    <p class="mt-1 text-sm font-normal text-gray-500">Browse a list of articles with their details.</p>
                </caption>
                <thead class="text-xs text-gray-700 uppercase bg-gray-100">
                <tr>
                    <th scope="col" class="px-6 py-3">ID</th>
                    <th scope="col" class="px-6 py-3">Title</th>
                    <th scope="col" class="px-6 py-3">Author</th>
                    <th scope="col" class="px-6 py-3">Content Preview</th>
                    <th scope="col" class="px-6 py-3">View</th>
                    <th scope="col" class="px-6 py-3">Edit</th>
                    <th scope="col" class="px-6 py-3">Delete</th>
                </tr>
                </thead>
                <tbody>
                <%
                    List<Article> articles = (List<Article>) request.getAttribute("articles");
                    for (Article article : articles) {
                %>
                <tr class="border-b">
                    <td class="px-6 py-4"><%= article.getId() %></td>
                    <td class="px-6 py-4"><%= article.getTitle() %></td>
                    <td class="px-6 py-4"><%= article.getAuthor().getName() %></td>
                    <td class="px-6 py-4"><%= article.getContent() != null && article.getContent().length() > 100
                            ? article.getContent().substring(0, 100) + "..." : article.getContent() %></td>
                    <td class="px-6 py-4">
                        <a href="<%= request.getContextPath() %>/article/view?id=<%= article.getId() %>"
                           class="btn btn-sm btn-info text-blue-600 hover:underline">
                            <i class="fas fa-eye"></i> View
                        </a>
                    </td>
                    <td class="px-6 py-4">
                        <a href="<%= request.getContextPath() %>/article/edit?id=<%= article.getId() %>"
                           class="btn btn-sm btn-warning text-yellow-600 hover:underline">
                            <i class="fas fa-edit"></i> Edit
                        </a>
                    </td>
                    <td class="px-6 py-4">
                        <form action="<%= request.getContextPath() %>/article/delete" method="post" style="display:inline;">
                            <input type="hidden" name="id" value="<%= article.getId() %>">
                            <button type="submit" class="btn btn-sm btn-danger text-red-600 hover:underline">
                                <i class="fas fa-trash-alt"></i> Delete
                            </button>
                        </form>
                    </td>
                </tr>
                <%
                    }
                %>
                </tbody>
            </table>
        </div>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
<script>
    $(document).ready(function() {
        $('#articleTable').DataTable({
            "paging": true,
            "pageLength": 10,
            "lengthMenu": [10, 25, 50, 100],
            "searching": true,
            "ordering": true
        });
    });
</script>
</body>
</html>
