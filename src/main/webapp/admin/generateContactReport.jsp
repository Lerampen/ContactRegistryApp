

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.List, com.registry.contactregistryapp.dao.ContactDAO" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    ContactDAO contactDAO = new ContactDAO();
    List<String> counties = contactDAO.getAllCounties();
    pageContext.setAttribute("counties", counties);
%>
<!DOCTYPE html>
<html>
<head>
    <title>Generate Contact Report</title>
    <style>
      
        body {
            font-family: sans-serif;
            margin: 20px;
        }
        .container {
            max-width: 600px;
            margin: 0 auto;
        }
        .form-group {
            margin-bottom: 15px;
        }
        label {
            display: block;
            margin-bottom: 5px;
        }
        select {
            width: 100%;
            padding: 8px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        button {
            padding: 10px 20px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
        }
        button:hover {
            background-color: #0056b3;
        }
        h2 {
            text-align: center;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Generate Contact Report</h2>
        <form action="<%= request.getContextPath() %>/ContactReportServlet" method="get" target="_blank">
            <div class="form-group">
                <label for="county">Filter by County:</label>
                <select name="county" id="county">
                    <option value="All Counties">All Counties</option>
                    <c:forEach var="county" items="${counties}">
                        <option value="${county}">${county}</option>
                    </c:forEach>
                </select>
            </div>
            <button type="submit">Generate Report</button>
        </form>
    </div>
</body>
</html>
