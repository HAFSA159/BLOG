<%
    // Set response headers to prevent caching
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
    response.setHeader("Pragma", "no-cache"); // HTTP 1.0
    response.setDateHeader("Expires", 0); // Proxies
%>
<%
    // Check if the session attribute "loggedInUser" is set
    if (session.getAttribute("loggedInUser") == null) {
        response.sendRedirect("login.jsp");
        return; // Ensure no further processing happens after redirect
    }
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard - Home</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
    <style>
        .blur-bg {
            background-image: url('images/back.jpg');
            background-size: cover;
            background-position: center;
            filter: blur(8px);
            -webkit-filter: blur(8px);
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            z-index: 1;
        }
        .content {
            position: relative;
            z-index: 2;
        }
    </style>
</head>
<body class="bg-gray-50 dark:bg-slate-900">
<!-- Background blur -->
<div class="blur-bg"></div>

<!-- Main Content -->
<div class="content flex items-center justify-center h-screen">
    <div class="bg-white/70 dark:bg-gray-800/70 p-8 rounded-lg shadow-lg w-full max-w-3xl">
        <!-- Welcome Message -->
        <h1 class="text-4xl font-bold text-white dark:text-yellow-300">Welcome, Our Editor</h1>
        <p class="mt-4 text-lg text-gray-200 dark:text-gray-400">Here's what you can do today:</p>


        <!-- Navigation Links -->
        <div class="grid grid-cols-2 gap-4 mt-6">
            <a href="<c:url value='/article/list'/>" class="flex items-center gap-4 p-4 bg-indigo-100 text-indigo-800 dark:bg-indigo-800 dark:text-indigo-50 rounded-lg shadow hover:shadow-md">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 7h18M3 12h18m-7 5h7" />
                </svg>
                <span>View Articles</span>
            </a>
            <a href="<c:url value='/article/create'/>" class="flex items-center gap-4 p-4 bg-green-100 text-green-800 dark:bg-green-800 dark:text-green-50 rounded-lg shadow hover:shadow-md">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.5v15m7.5-7.5H4.5" />
                </svg>
                <span>Create Article</span>
            </a>
            <a href="<c:url value='/author/list'/>" class="flex items-center gap-4 p-4 bg-blue-100 text-blue-800 dark:bg-blue-800 dark:text-blue-50 rounded-lg shadow hover:shadow-md">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 7h16M4 12h16m-7 5h7" />
                </svg>
                <span>View Authors</span>
            </a>
            <a href="<c:url value='/logout'/>" class="flex items-center gap-4 p-4 bg-red-100 text-red-800 dark:bg-red-800 dark:text-red-50 rounded-lg shadow hover:shadow-md">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H3" />
                </svg>
                <span>Log Out</span>
            </a>
        </div>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
