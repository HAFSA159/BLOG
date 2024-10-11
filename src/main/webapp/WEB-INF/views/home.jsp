<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Blog Home</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet" />
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" rel="stylesheet" />
</head>
<body class="bg-gray-100">
    <!-- Navbar -->
    <nav class="bg-gray-800 p-4">
        <div class="container mx-auto flex justify-between items-center">
            <a href="${pageContext.request.contextPath}/" class="text-white text-2xl font-bold">Blog App</a>
            <div>
                <c:choose>
                    <c:when test="${empty sessionScope.loggedInUser}">
                        <a href="${pageContext.request.contextPath}/login.jsp" class="text-white mr-4 hover:text-gray-300">Login</a>
                        <a href="${pageContext.request.contextPath}/register.jsp" class="text-white hover:text-gray-300">Register</a>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/index.jsp" class="text-white mr-4 hover:text-gray-300">Dashboard</a>
                        <a href="${pageContext.request.contextPath}/logout" class="text-white hover:text-gray-300">Logout</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </nav>

    <div class="container mx-auto px-4 py-8">
        <h1 class="text-4xl font-bold mb-8 text-center text-gray-800">Welcome to Our Blog</h1>

        <!-- Search Form -->
        <form action="${pageContext.request.contextPath}/home" method="get" class="mb-8 flex justify-center">
            <input
                type="text"
                name="query"
                value="${query}"
                placeholder="Search articles..."
                class="px-4 py-2 w-1/2 border rounded-l-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            <button
                type="submit"
                class="px-4 py-2 bg-blue-500 text-white rounded-r-md hover:bg-blue-600 transition duration-300"
            >
                <i class="fas fa-search mr-2"></i>Search
            </button>
        </form>

        <!-- Articles List -->
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            <c:forEach var="article" items="${articles}">
                <c:if test="${article.status eq 'published'}">
                    <div class="bg-white rounded-lg shadow-md overflow-hidden transition duration-300 hover:shadow-xl">
                        <c:if test="${not empty article.imagePath}">
                            <img src="${pageContext.request.contextPath}${article.imagePath}" alt="${article.title}" class="w-full h-48 object-cover">
                        </c:if>
                        <div class="p-6">
                            <h2 class="text-2xl font-semibold mb-2 text-gray-800">${article.title}</h2>
                            <p class="text-gray-600 mb-4">
                                <i class="fas fa-user-circle mr-2"></i>${article.author.name}
                                <span class="mx-2">|</span>
                                <i class="far fa-calendar-alt mr-2"></i>${article.creationDate}
                            </p>
                            <p class="mb-4 text-gray-700">${fn:substring(article.content, 0, 150)}...</p>
                            <div class="flex justify-between items-center">
                                <a
                                    href="${pageContext.request.contextPath}/article/view?id=${article.id}"
                                    class="text-blue-500 hover:text-blue-700 transition duration-300"
                                >
                                    Read More <i class="fas fa-arrow-right ml-1"></i>
                                </a>
                                <span class="text-gray-600">
                                    <i class="far fa-comment mr-1"></i>${article.comments != null ? fn:length(article.comments) : 0} comments
                                </span>
                            </div>
                        </div>
                    </div>
                </c:if>
            </c:forEach>
        </div>

        <!-- Pagination -->
        <c:if test="${noOfPages > 1}">
            <div class="flex justify-center space-x-2 mt-8">
                <c:forEach begin="1" end="${noOfPages}" var="i">
                    <a
                        href="${pageContext.request.contextPath}/home?page=${i}&query=${query}"
                        class="px-3 py-2 ${currentPage eq i ? 'bg-blue-500 text-white' : 'bg-white text-blue-500'} border rounded hover:bg-blue-100 transition duration-300"
                    >${i}</a>
                </c:forEach>
            </div>
        </c:if>
    </div>
</body>
</html>