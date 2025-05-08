<%-- 
    Document   : contactDetails
    Created on : May 6, 2025, 11:47:14 AM
    Author     : Lera
--%>

<%-- contactDetails.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Contact Details</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>

<div class="nav">
    <a href="${pageContext.request.contextPath}/index.jsp">Home</a>
    <a href="${pageContext.request.contextPath}/contact/contactList.jsp">Contacts</a>
    <a href="${pageContext.request.contextPath}/contact/addContact.jsp">Add Contact</a>
</div>

<div class="contact-card">
    <h3>Contact Details</h3>

    <div class="contact-detail">
        <strong>Full Name:</strong> ${contact.fullName}
    </div>

    <div class="contact-detail">
        <strong>Phone:</strong> <a href="tel:${contact.phoneNumber}">${contact.phoneNumber}</a>
    </div>

    <div class="contact-detail">
        <strong>Email:</strong> <a href="mailto:${contact.email}">${contact.email}</a>
    </div>

    <div class="contact-detail">
        <strong>ID Number:</strong> ${contact.idNumber}
    </div>

    <div class="contact-detail">
        <strong>Date of Birth:</strong> <fmt:formatDate value="${contact.dateOfBirth}" pattern="MMMM d, yyyy" />
    </div>

    <div class="contact-detail">
        <strong>Gender:</strong> ${contact.gender}
    </div>

    <div class="contact-detail">
        <strong>County of Residence:</strong> ${contact.countyOfResidence}
    </div>
</div>

</body>
</html>
