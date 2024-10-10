<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
    <title>Update Author</title>
</head>
<body class="bg-gray-100">
<div class="container mx-auto p-8">
    <h1 class="text-2xl font-bold mb-6">Update Author</h1>

    <c:if test="${not empty errorMessage}">
        <div class="bg-red-500 text-white p-4 mb-4 rounded">
                ${errorMessage}
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/author" method="post" class="bg-white p-6 rounded shadow-md">
        <input type="hidden" name="action" value="update"/>
        <input type="hidden" name="authorId" value="${author.id}"/>

        <div class="mb-4">
            <label for="name" class="block text-gray-700 text-sm font-bold mb-2">Name</label>
            <input type="text" id="name" name="name" value="${author.name}" required class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"/>
        </div>

        <div class="mb-4">
            <label for="email" class="block text-gray-700 text-sm font-bold mb-2">Email</label>
            <input type="email" id="email" name="email" value="${author.email}" required class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"/>
        </div>

        <div class="mb-4">
            <label for="password" class="block text-gray-700 text-sm font-bold mb-2">Password</label>
            <input type="password" id="password" name="password" required class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"/>
        </div>

        <div class="mb-4">
            <label for="birthdate" class="block text-gray-700 text-sm font-bold mb-2">Birthdate</label>
            <input type="date" id="birthdate" name="birthdate" value="${author.birthdate}" required class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"/>
        </div>

        <div class="flex items-center justify-between">
            <button type="submit" class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">Update Author</button>
        </div>
    </form>
</div>
</body>
</html>
