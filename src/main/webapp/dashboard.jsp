<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<aside class="sidebar w-64 h-screen p-6 text-white">
    <h2 class="text-3xl font-bold">Dashboard</h2>
    <nav class="mt-6">
        <ul>
            <li class="mb-4">
                <a href="${pageContext.request.contextPath}/author?action=create" class="block px-4 py-2 rounded-lg bg-purple-300 hover:bg-purple-400 text-gray-900">
                    Create Author
                </a>
            </li>
            <li class="mb-4">
                <a href="${pageContext.request.contextPath}/article/list" class="block px-4 py-2 rounded-lg bg-indigo-400 hover:bg-indigo-500 text-white">
                    View Articles
                </a>
            </li>
            <li class="mb-4">
                <a href="${pageContext.request.contextPath}/article/create" class="block px-4 py-2 rounded-lg bg-green-400 hover:bg-green-500 text-white">
                    Create Article
                </a>
            </li>
            <li class="mb-4">
                <a href="${pageContext.request.contextPath}/author/list" class="block px-4 py-2 rounded-lg bg-blue-400 hover:bg-blue-500 text-white">
                    View Authors
                </a>
            </li>
            <li class="mb-4">
                <a href="${pageContext.request.contextPath}/comment/list" class="block px-4 py-2 rounded-lg bg-yellow-400 hover:bg-yellow-500 text-white">
                    Manage Comments
                </a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/logout" class="block px-4 py-2 rounded-lg bg-red-400 hover:bg-red-500 text-white">
                    Log Out
                </a>
            </li>
        </ul>

    </nav>
</aside>
