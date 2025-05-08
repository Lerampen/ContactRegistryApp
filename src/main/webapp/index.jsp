<%-- 
    Document   : index
    Created on : May 6, 2025, 11:50:43 AM
    Author     : Lera
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${param.title}</title>
    <style>
        body {
            font-family: sans-serif;
            background-color: #f8f8f8;
            margin: 0;
            padding: 0;
        }
        .navbar {
            background-color: #333;
            color: white;
            padding: 10px 0;
        }
        .navbar-brand {
            font-size: 1.2em;
            margin-left: 10px;
            text-decoration: none;
            color: white;
        }
        .navbar-nav {
            list-style: none;
            padding: 0;
            margin: 0;
            display: flex;
        }
        .nav-item {
            margin: 0 10px;
        }
        .nav-link {
            color: white;
            text-decoration: none;
        }
        .main-content {
            background-color: white;
            padding: 20px;
            margin: 20px;
            border-radius: 5px;
            box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
        }
        h1 {
            margin-top: 0;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <nav class="navbar">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/">Contact Registry</a>
        <ul class="navbar-nav">
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/">Home</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/ContactServlet"> View Contacts</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/contact/new">Add New Contact</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/admin/dashboard">Admin Dashboard</a>
            </li>
           <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/admin/generateContactReport.jsp">Generate Report</a>
            </li> 
        </ul>
    </nav>

    <div class="container main-content">
        <h1>${param.title}</h1>
        <c:if test="${not empty content and content ne 'index.jsp'}">
            <jsp:include page="${content}" />
        </c:if>
    </div>
</body>
</html>