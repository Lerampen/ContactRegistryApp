<%-- 
    Document   : addContact
    Created on : May 6, 2025, 11:47:29 AM
    Author     : Lera
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="pageTitle" value="${empty contact ? 'Add New Contact' : 'Edit Contact'}" />

<jsp:include page="../index.jsp">
    <jsp:param name="title" value="${pageTitle}" />
    <jsp:param name="content" value="/contact/addContact.jsp" />
</jsp:include>

<%-- Contact Form Content (included in layout) --%>
<div>
    <h3>${empty contact ? 'Add New Contact' : 'Edit Contact'}</h3>

    <c:if test="${not empty error}">
        <div style="color: red;">
            ${error}
        </div>
    </c:if>
    
    <form action="${pageContext.request.contextPath}/ContactServlet" method="post" id="contactForm">
        <c:if test="${not empty contact}">
            <input type="hidden" name="id" value="${contact.id}">
        </c:if>
        
        <div>
            <label for="fullName">Full Name <span style="color: red;">*</span></label>
            <input type="text" id="fullName" name="fullName" value="${contact.fullName}" required>
        </div>
        
        <div>
            <label for="phoneNumber">Phone Number <span style="color: red;">*</span></label>
            <input type="tel" id="phoneNumber" name="phoneNumber" value="${contact.phoneNumber}" required>
        </div>

        <div>
            <label for="email">Email <span style="color: red;">*</span></label>
            <input type="email" id="email" name="email" value="${contact.email}" required>
        </div>

        <div>
            <label for="idNumber">ID Number <span style="color: red;">*</span></label>
            <input type="text" id="idNumber" name="idNumber" value="${contact.idNumber}" required>
        </div>

        <div>
            <label for="dateOfBirth">Date of Birth <span style="color: red;">*</span></label>
            <input type="date" id="dateOfBirth" name="dateOfBirth" value="<fmt:formatDate value="${contact.dateOfBirth}" pattern="yyyy-MM-dd" />" required>
        </div>

        <div>
            <label for="gender">Gender <span style="color: red;">*</span></label>
            <select id="gender" name="gender" required>
                <option value="" ${empty contact ? 'selected' : ''}>Select Gender</option>
                <option value="Male" ${contact.gender == 'Male' ? 'selected' : ''}>Male</option>
                <option value="Female" ${contact.gender == 'Female' ? 'selected' : ''}>Female</option>
            </select>
        </div>

        <div>
            <label for="countyOfResidence">County of Residence <span style="color: red;">*</span></label>
            <select id="countyOfResidence" name="countyOfResidence" required>
                <option value="" ${empty contact ? 'selected' : ''}>Select County</option>
                <option value="Baringo" ${contact.countyOfResidence == 'Baringo' ? 'selected' : ''}>Baringo</option>
                <option value="Bomet" ${contact.countyOfResidence == 'Bomet' ? 'selected' : ''}>Bomet</option>
                <option value="Bungoma" ${contact.countyOfResidence == 'Bungoma' ? 'selected' : ''}>Bungoma</option>
                <!-- Add other counties similarly -->
            </select>
        </div>

        <div>
            <a href="${pageContext.request.contextPath}/ContactServlet">Back to List</a>
            <button type="submit">Save Contact</button>
        </div>
    </form>
</div>

<%-- JavaScript for contact form validation --%>
<script>
    document.getElementById('contactForm').addEventListener('submit', function(event) {
        let isValid = true;

        const phoneNumber = document.getElementById('phoneNumber').value;
        if (!/^\d{10,12}$/.test(phoneNumber.replace(/[- ]/g, ''))) {
            alert('Please enter a valid phone number (10-12 digits)');
            isValid = false;
        }

        const email = document.getElementById('email').value;
        if (!/^\S+@\S+\.\S+$/.test(email)) {
            alert('Please enter a valid email address');
            isValid = false;
        }

        const idNumber = document.getElementById('idNumber').value;
        if (idNumber.length < 5) {
            alert('ID number must be at least 5 characters');
            isValid = false;
        }

        const dob = new Date(document.getElementById('dateOfBirth').value);
        const today = new Date();
        if (dob >= today) {
            alert('Date of birth must be in the past');
            isValid = false;
        }

        if (!isValid) {
            event.preventDefault();
        }
    });
</script>

