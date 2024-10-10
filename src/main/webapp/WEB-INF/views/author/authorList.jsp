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
</head>
<body class="bg-gray-100">

<div class="container mx-auto p-6">
    <div class="relative overflow-x-auto shadow-md sm:rounded-lg">
        <table id="authorTable" class="w-full text-sm text-left rtl:text-right text-gray-500 dark:text-gray-400">
            <caption class="p-5 text-lg font-semibold text-left rtl:text-right text-gray-900 bg-white dark:text-white dark:bg-gray-800">
                Author List
                <p class="mt-1 text-sm font-normal text-gray-500 dark:text-gray-400">Browse a list of authors with their details.</p>
            </caption>
            <thead class="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
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
            <tr id="author-<%= author.getId() %>" class="bg-white border-b dark:bg-gray-800 dark:border-gray-700">
                <td class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-white">
                    <%= author.getId() %>
                </td>
                <td class="px-6 py-4">
                    <%= author.getName() %>
                </td>
                <td class="px-6 py-4">
                    <%= author.getEmail() %>
                </td>
                <td class="px-6 py-4">
                    <%= author.getBirthdate() %>
                </td>
                <td class="px-6 py-4">
                    <form action="<%= request.getContextPath() %>/author" method="post" class="inline">
                        <input type="hidden" name="authorId" value="<%= author.getId() %>">
                        <select name="newRole" onchange="this.form.submit()" class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg">
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
                <td class="px-6 py-4 text-right">
                    <a href="${pageContext.request.contextPath}/author?action=edit&id=<%= author.getId() %>">Edit</a>
                </td>


                <td class="px-6 py-4 text-right">
                    <form action="<%= request.getContextPath() %>/author?action=delete" method="post" class="inline">
                        <input type="hidden" name="id" value="<%= author.getId() %>">
                        <button type="submit" class="font-medium text-red-600 dark:text-red-500 hover:underline">Delete</button>
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
