<%
    // Set response headers to prevent caching
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
    response.setHeader("Pragma", "no-cache"); // HTTP 1.0
    response.setDateHeader("Expires", 0); // Proxies

    // Check if the session attribute "loggedInUser" is set
    if (session.getAttribute("loggedInUser") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    // Get the user's role from the session
    String userRole = (String) session.getAttribute("userRole");
    if (userRole == null) {
        userRole = ""; // Avoid null values
    }
    request.setAttribute("userRole", userRole); // Set the attribute for JSTL access

    // Redirect users who are not Editors
    if (!"EDITOR".equalsIgnoreCase(userRole)) {
        response.sendRedirect("/Blog_war");
        return;
    }
%>
