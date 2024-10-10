<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Author</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
</head>
<body class="bg-gray-100 flex items-center justify-center min-h-screen">
<div class="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4 w-full max-w-lg">
    <h2 class="text-2xl font-bold mb-6 text-center">Edit Author</h2>
    <form action="${pageContext.request.contextPath}/author?action=update" method="post">
        <input type="hidden" name="authorId" value="${author.id}">

        <div class="mb-4">
            <label class="block text-gray-700 text-sm font-bold mb-2" for="name">
                Name
            </label>
            <input type="text" name="name" id="name" value="${author.name}" required
                   class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
        </div>

        <div class="mb-4">
            <label class="block text-gray-700 text-sm font-bold mb-2" for="email">
                Email
            </label>
            <input type="email" name="email" id="email" value="${author.email}" required
                   class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
        </div>

        <div class="mb-4">
            <label class="block text-gray-700 text-sm font-bold mb-2" for="birthdate">
                Birthdate
            </label>
            <input type="date" name="birthdate" id="birthdate" value="${author.birthdate != null ? author.birthdate.toString() : ''}" required
                   class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
        </div>

        <div class="flex items-center justify-between">
            <button type="submit"
                    class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">
                Update Author
            </button>
            <a href="${pageContext.request.contextPath}/author?action=list" class="text-blue-500 hover:text-blue-800">
                Cancel
            </a>
        </div>
    </form>
    <c:if test="${not empty errorMessage}">
        <div class="mt-4 text-red-500 text-sm text-center">
                ${errorMessage}
        </div>
    </c:if>
</div>
</body>
</html>
