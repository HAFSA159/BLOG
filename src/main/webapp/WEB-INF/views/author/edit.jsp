<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Author</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
    <style>
        .blur-bg {
            background-image: url('/images/back.jpg');
            background-size: cover;
            background-position: center;
            filter: blur(10px);
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
        .form-container {
            background-color: rgba(255, 255, 255, 0.9);
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
<div class="blur-bg"></div>

<div class="content flex">
    <!-- Sidebar -->
    <%@ include file="/dashboard.jsp" %>

    <!-- Main Content -->
    <div class="flex-1 p-6 main-content rounded-lg">
        <h2 class="text-3xl font-semibold italic text-white mb-6">Edit Author</h2>

        <div class="form-container p-8 w-full max-w-lg mx-auto">
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
    </div>
</div>

</body>
</html>
