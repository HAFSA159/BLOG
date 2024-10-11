<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.blogapp.model.Author" %>
<%@ page import="com.blogapp.model.AuthorRole" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.datatables.net/1.13.6/css/jquery.dataTables.min.css">
    <title>Authors List</title>
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
    <!-- Authors Table Content -->
    <div class="flex-1 p-6 main-content rounded-lg">
        <div class="table-container overflow-x-auto p-4">
            <table id="authorTable" class="w-full text-sm text-left text-gray-800">
                <caption class="p-5 text-lg font-semibold text-gray-900">
                    Author List
                    <p class="mt-1 text-sm font-normal text-gray-500">Browse a list of authors with their details.</p>
                </caption>
                <thead class="text-xs text-gray-700 uppercase bg-gray-100">
                <tr>
                    <th scope="col" class="px-6 py-3">ID</th>
                    <th scope="col" class="px-6 py-3">Name</th>
                    <th scope="col" class="px-6 py-3">Email</th>
                    <th scope="col" class="px-6 py-3">Birthdate</th>
                    <th scope="col" class="px-6 py-3">Role</th>
                    <th scope="col" class="px-6 py-3">Edit</th>
                    <th scope="col" class="px-6 py-3">Delete</th>
                </tr>
                </thead>
                <tbody>
                <%
                    List<Author> authors = (List<Author>) request.getAttribute("authors");
                    for (Author author : authors) {
                %>
                <tr class="border-b">
                    <td class="px-6 py-4"><%= author.getId() %></td>
                    <td class="px-6 py-4"><%= author.getName() %></td>
                    <td class="px-6 py-4"><%= author.getEmail() %></td>
                    <td class="px-6 py-4"><%= author.getBirthdate() %></td>
                    <td class="px-6 py-4">
                        <form action="<%= request.getContextPath() %>/author" method="post">
                            <input type="hidden" name="authorId" value="<%= author.getId() %>">
                            <select name="newRole" onchange="this.form.submit()" class="bg-gray-100 rounded">
                                <%
                                    for (AuthorRole role : AuthorRole.values()) {
                                        boolean selected = role == author.getRole();
                                %>
                                <option value="<%= role.name() %>" <%= selected ? "selected" : "" %>><%= role.name() %></option>
                                <%
                                    }
                                %>
                            </select>
                        </form>
                    </td>
                    <td class="px-6 py-4">
                        <a href="${pageContext.request.contextPath}/author?action=edit&id=<%= author.getId() %>" class="text-blue-600 hover:underline">Edit</a>
                    </td>
                    <td class="px-6 py-4">
                        <form action="<%= request.getContextPath() %>/author?action=delete" method="post">
                            <input type="hidden" name="id" value="<%= author.getId() %>">
                            <button type="submit" class="text-red-600 hover:underline">Delete</button>
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
        $('#authorTable').DataTable({
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
