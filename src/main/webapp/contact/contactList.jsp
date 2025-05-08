<%-- 
    Document   : contactList.jsp
    Created on : May 6, 2025, 11:46:42 AM
    Author     : Lera
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="actions">
    <a href="${pageContext.request.contextPath}/ContactServlet/new" class="btn">Add Contact</a>

<form action="${pageContext.request.contextPath}/ContactServlet/filter" method="get" class="filter"> 
    <select name="county">
            <option value="">All Counties</option>
            <c:forEach items="${counties}" var="county">
                <option value="${county}" ${county eq selectedCounty ? 'selected' : ''}>${county}</option>
            </c:forEach>
        </select>
        <button type="submit" class="btn">Filter</button>
    </form>
</div>

<table class="table">
    <thead>
        <tr>
            <th>ID</th>
            <th>Full Name</th>
            <th>Phone</th>
            <th>Email</th>
            <th>County</th>
            <th>Gender</th>
            <th>Actions</th>
        </tr>
    </thead>
    <tbody>
        <c:choose>
            <c:when test="${empty contacts}">
                <tr><td colspan="7">No contacts found</td></tr>
            </c:when>
            <c:otherwise>
                <c:forEach items="${contacts}" var="c">
                    <tr>
                        <td>${c.id}</td>
                        <td>${c.fullName}</td>
                        <td>${c.phoneNumber}</td>
                        <td>${c.email}</td>
                        <td>${c.countyOfResidence}</td>
                        <td>${c.gender}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/ContactServlet/${c.id}" class="btn-small">View</a>
                            <a href="${pageContext.request.contextPath}/ContactServlet/edit/${c.id}" class="btn-small">Edit</a>
                            <form action="${pageContext.request.contextPath}/ContactServlet/delete" method="post" style="display:inline;">
                                <input type="hidden" name="id" value="${c.id}">
                                 <input type="hidden" name="action" value="delete">
                                <button type="submit" class="btn-small">Delete</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </tbody>
</table>

