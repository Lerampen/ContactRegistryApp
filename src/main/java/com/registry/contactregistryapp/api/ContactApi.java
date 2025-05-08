package com.registry.contactregistryapp.api;

import com.registry.contactregistryapp.dao.ContactDAO;
import com.registry.contactregistryapp.model.Contact;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import jakarta.servlet.ServletException; 
import jakarta.servlet.http.HttpServlet; 
import jakarta.servlet.http.HttpServletRequest; 
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class ContactApi extends HttpServlet {
    private ContactDAO contactDAO;
    private Gson gson;
    private SimpleDateFormat dateFormat;
    
    @Override
    public void init() throws ServletException {
        super.init();
        contactDAO = new ContactDAO();
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        String pathInfo = request.getPathInfo();
        
        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                // Get all contacts
                List<Contact> contacts = contactDAO.findAll();
                out.print(gson.toJson(contacts));
            } else if (pathInfo.startsWith("/county/")) {
                // Get contacts by county
                String county = pathInfo.substring("/county/".length());
                List<Contact> contacts = contactDAO.findByCounty(county);
                out.print(gson.toJson(contacts));
            } else if (pathInfo.equals("/recent")) {
                // Get recent contacts (default 5)
                List<Contact> contacts = contactDAO.findRecent();
                out.print(gson.toJson(contacts));
            } else if (pathInfo.equals("/stats/gender")) {
                // Get gender statistics
                out.print(gson.toJson(contactDAO.getGenderStats()));
            } else if (pathInfo.equals("/stats/county")) {
                // Get county statistics
                out.print(gson.toJson(contactDAO.getCountyStats()));
            } else if (pathInfo.equals("/counties")) {
                // Get all unique counties
                List<String> counties = contactDAO.getAllCounties();
                out.print(gson.toJson(counties));
            } else {
                try {
                    // Get contact by ID
                    Long id = Long.parseLong(pathInfo.substring(1));
                    Contact contact = contactDAO.findById(id);
                    if (contact != null) {
                        out.print(gson.toJson(contact));
                    } else {
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        out.print("{\"error\": \"Contact not found\"}");
                    }
                } catch (NumberFormatException e) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("{\"error\": \"Invalid contact ID\"}");
                }
            }
        } catch (SQLException e) {
            handleSQLException(response, out, e);
        }
        
        out.flush();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            StringBuilder buffer = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            
            String payload = buffer.toString();
            Contact contact = gson.fromJson(payload, Contact.class);
            
            Contact createdContact = contactDAO.create(contact);
            
            response.setStatus(HttpServletResponse.SC_CREATED);
            out.print(gson.toJson(createdContact));
        } catch (SQLException e) {
            handleSQLException(response, out, e);
        }
        
        out.flush();
    }
    
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"Contact ID is required\"}");
        } else {
            try {
                Long id = Long.parseLong(pathInfo.substring(1));
                Contact existingContact = contactDAO.findById(id);
                
                if (existingContact != null) {
                    StringBuilder buffer = new StringBuilder();
                    BufferedReader reader = request.getReader();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }
                    
                    String payload = buffer.toString();
                    Contact updatedContact = gson.fromJson(payload, Contact.class);
                    updatedContact.setId(id);
                    
                    Contact result = contactDAO.update(updatedContact);
                    out.print(gson.toJson(result));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"error\": \"Contact not found\"}");
                }
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"Invalid contact ID\"}");
            } catch (SQLException e) {
                handleSQLException(response, out, e);
            }
        }
        
        out.flush();
    }
    
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"Contact ID is required\"}");
        } else {
            try {
                Long id = Long.parseLong(pathInfo.substring(1));
                boolean deleted = contactDAO.delete(id);
                
                if (deleted) {
                    out.print("{\"message\": \"Contact deleted successfully\"}");
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"error\": \"Contact not found\"}");
                }
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"Invalid contact ID\"}");
            } catch (SQLException e) {
                handleSQLException(response, out, e);
            }
        }
        
        out.flush();
    }
    
    /**
     * Helper method to handle SQL exceptions
     */
    private void handleSQLException(HttpServletResponse response, PrintWriter out, SQLException e) throws IOException {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        out.print("{\"error\": \"Database error: " + e.getMessage() + "\"}");
        e.printStackTrace();
    }
}
