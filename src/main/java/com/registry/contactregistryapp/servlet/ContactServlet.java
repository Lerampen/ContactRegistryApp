/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.registry.contactregistryapp.servlet;

import com.registry.contactregistryapp.dao.ContactDAO;
import com.registry.contactregistryapp.model.Contact;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import jakarta.servlet.ServletException; 
import jakarta.servlet.http.HttpServlet; 
import jakarta.servlet.http.HttpServletRequest; 
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author Lera
 */
public class ContactServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private ContactDAO contactDAO;
    private SimpleDateFormat dateFormat;

    

    @Override
    public void init() {
        contactDAO = new ContactDAO();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        
    }
    
  

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            String pathInfo = request.getPathInfo();
                System.out.println("doGet called with pathInfo: " + pathInfo); 

            
             try {
           if (pathInfo == null || pathInfo.equals("/")) {
    // List all contacts
    List<Contact> contacts = contactDAO.findAll();
    List<String> counties = contactDAO.getAllCounties();
    request.setAttribute("contacts", contacts);
    request.setAttribute("counties", counties);         
    request.getRequestDispatcher("/contact/contactList.jsp").forward(request, response);
    

} else if (pathInfo.equals("/new")) {
                // Show form to create a new contact
                request.getRequestDispatcher("/contact/addContact.jsp").forward(request, response);
            } else if (pathInfo.startsWith("/edit/")) {
                // Show form to edit an existing contact
                String idStr = pathInfo.substring(6); 
                Long id = Long.parseLong(idStr);
                Contact contact = contactDAO.findById(id);
                
                if (contact != null) {
                    request.setAttribute("contact", contact);
                    request.getRequestDispatcher("/contact/addContact.jsp").forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            } else if (pathInfo.equals("/filter")) {
                // Filter contacts by county
                String county = request.getParameter("county");
                if (county != null && !county.isEmpty()) {
                    List<Contact> contacts = contactDAO.findByCounty(county);
                    request.setAttribute("contacts", contacts);
                    request.setAttribute("selectedCounty", county);
                } else {
                    List<Contact> contacts = contactDAO.findAll();
                    request.setAttribute("contacts", contacts);
                }
                
                // Get all counties for the filter dropdown
                List<String> counties = contactDAO.getAllCounties();
                request.setAttribute("counties", counties);
                
                request.getRequestDispatcher("/contact/contactList.jsp").forward(request, response);
            } else {
                // Try to get a specific contact by ID
                String idStr = pathInfo.substring(1); 
                Long id = Long.parseLong(idStr);
                Contact contact = contactDAO.findById(id);
                
                if (contact != null) {
                    request.setAttribute("contact", contact);
                    request.getRequestDispatcher("/contact/contactDetails.jsp").forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            }
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        String idToDeleteStr = request.getParameter("id");
        
        
        
        if ("delete".equals(action) && idToDeleteStr != null && !idToDeleteStr.isEmpty()) {
            try {
                Long idToDelete = Long.parseLong(idToDeleteStr);
                contactDAO.delete(idToDelete);
                response.sendRedirect(request.getContextPath() + "/ContactServlet");
                return;
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID for deletion");
                return;
            } catch (SQLException e) {
                throw new ServletException("Database error during deletion", e);
            }
        } else {
         String idStr = request.getParameter("id");
        String fullName = request.getParameter("fullName");
        String phoneNumber = request.getParameter("phoneNumber");
        String email = request.getParameter("email");
        String idNumber = request.getParameter("idNumber");
        String dobStr = request.getParameter("dateOfBirth");
        String gender = request.getParameter("gender");
        String county = request.getParameter("countyOfResidence");
        
        try {
            // Only parse the date if it's not null (i.e., during add or update)
                Date dob = null;
                if (dobStr != null && !dobStr.isEmpty()) {
                    dob = dateFormat.parse(dobStr);
                }
            
            Contact contact = new Contact();
            contact.setFullName(fullName);
            contact.setPhoneNumber(phoneNumber);
            contact.setEmail(email);
            contact.setIdNumber(idNumber);
            contact.setDateOfBirth(dob);
            contact.setGender(gender);
            contact.setCountyOfResidence(county);
            
            if (idStr != null && !idStr.isEmpty()) {
                // Update existing contact
                contact.setId(Long.parseLong(idStr));
                contactDAO.update(contact);
            } else {
                // Create new contact
                contactDAO.create(contact);
            }
            
            response.sendRedirect(request.getContextPath() + "/contact/contactList.jsp");
        } catch (ParseException e) {
              request.setAttribute("error", "Invalid date format");
        // Determine if it was an add or edit operation to redirect to the correct form
        if (idStr != null && !idStr.isEmpty()) {
            // It was an edit, redirect back to the edit form
            response.sendRedirect(request.getContextPath() + "/contact/editContact.jsp?id=" + idStr + "&error=invalidDate");
        } else {
            // It was an add, redirect back to the add form
            response.sendRedirect(request.getContextPath() + "/contact/addContact.jsp?error=invalidDate");
        }
        return;
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

}
