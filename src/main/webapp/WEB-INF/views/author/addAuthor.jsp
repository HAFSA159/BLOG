<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
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
        <h2 class="text-3xl font-semibold italic text-white mb-6">Add New Author</h2>

        <div class="form-container p-8 w-full max-w-lg mx-auto">
            <form action="${pageContext.request.contextPath}/register" method="post">
                <input type="hidden" name="source" value="addAuthor" />

                <div class="mb-4">
                    <label class="block text-gray-700 text-sm font-bold mb-2" for="name">Name:</label>
                    <input type="text" name="name" id="name" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" placeholder="Author Name" required>
                </div>

                <div class="mb-4">
                    <label class="block text-gray-700 text-sm font-bold mb-2" for="email">Email:</label>
                    <input type="email" name="email" id="email" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" placeholder="Author Email" required>
                </div>

                <div class="mb-4">
                    <label class="block text-gray-700 text-sm font-bold mb-2" for="password">Password:</label>
                    <input type="password" name="password" id="password" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" placeholder="Password" required>
                </div>

                <div class="mb-4">
                    <label class="block text-gray-700 text-sm font-bold mb-2" for="re_pass">Confirm Password:</label>
                    <input type="password" name="re_pass" id="re_pass" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" placeholder="Confirm Password" required>
                </div>

                <div class="mb-4">
                    <label class="block text-gray-700 text-sm font-bold mb-2" for="birthdate">Birthdate:</label>
                    <input type="date" name="birthdateStr" id="birthdate" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" required>
                </div>

                <div class="mb-4">
                    <label class="block text-gray-700 text-sm font-bold mb-2" for="role">Role:</label>
                    <select name="role" id="role" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" required>
                        <option value="Contributor">Contributor</option>
                        <option value="Editor">Editor</option>
                    </select>
                </div>

                <div class="flex items-center justify-between">
                    <button type="submit" class="btn-primary py-2 px-4 rounded focus:outline-none focus:shadow-outline">
                        Add Author
                    </button>
                    <a href="${pageContext.request.contextPath}/dashboard" class="text-gray-600 hover:underline">Cancel</a>
                </div>
            </form>
        </div>
    </div>
</div>

</body>
</html>
