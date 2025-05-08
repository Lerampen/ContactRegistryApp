package com.registry.contactregistryapp.servlet;

import com.registry.contactregistryapp.dao.ContactDAO;
import com.registry.contactregistryapp.model.Contact;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection; // Import for testing

/**
 * Servlet for handling admin dashboard requests
 *
 */
public class AdminDashboardServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AdminDashboardServlet.class.getName());
    private ContactDAO contactDAO;

    @Override
    public void init() throws ServletException {
        try {
            super.init(); // Call super.init() first.
            contactDAO = new ContactDAO(); // initialize DAO
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error initializing AdminDashboardServlet: ", e);
            throw new ServletException("Failed to initialize AdminDashboardServlet", e); // Wrap it.
        }
    }

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

        // Initialize with empty/default values to prevent null pointer exceptions
        List<Contact> recentContacts = new ArrayList<>();
        Map<String, Integer> genderStats = new HashMap<>();
        Map<String, Integer> countyStats = new HashMap<>();

        // *** START: Temporary Database Connection Test ***
        Connection testConnection = null;
        try {
            testConnection = com.registry.contactregistryapp.util.DatabaseConnection.getConnection(); // Corrected
            if (testConnection != null) {
                LOGGER.info("DEBUG: Successfully obtained a database connection for testing in AdminDashboardServlet.");
                testConnection.close(); // Close it immediately after the test.
            } else {
                LOGGER.severe("ERROR: Failed to obtain a database connection for testing in AdminDashboardServlet.");
                request.setAttribute("errorMessage", "Failed to connect to the database. Check your database configuration.");
                // Forward to an error page or the dashboard, as appropriate
                RequestDispatcher dispatcher = request.getRequestDispatcher("/admin/dashboard.jsp"); //Or error page
                dispatcher.forward(request, response);
                return; // IMPORTANT: Exit the method after the error!
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "ERROR: SQLException during database connection test in AdminDashboardServlet: ", e);
            request.setAttribute("errorMessage", "Database connection test failed: " + e.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher("/admin/dashboard.jsp");  // Or error page
            dispatcher.forward(request, response);
            return; //Added return
        } finally {
           if (testConnection != null){
               try{
                   testConnection.close();
               } catch(SQLException ex){
                   LOGGER.log(Level.WARNING, "Error closing test connection", ex);
               }
           }
        }
        // *** END: Temporary Database Connection Test ***

        try {
            // Fetch the required data
            LOGGER.info("DEBUG: Fetching recent contacts...");
            recentContacts = contactDAO.findRecent();
            LOGGER.info("DEBUG: Fetched " + recentContacts.size() + " recent contacts.");

            LOGGER.info("DEBUG: Fetching gender statistics...");
            genderStats = contactDAO.getGenderStats();
            LOGGER.info("DEBUG: Fetched gender statistics: " + genderStats);

            LOGGER.info("DEBUG: Fetching county statistics...");
            countyStats = contactDAO.getCountyStats();
            LOGGER.info("DEBUG: Fetched county statistics: " + countyStats);

        } catch (SQLException e) {
            // Log the exception properly
            LOGGER.log(Level.SEVERE, "Database error while loading dashboard data", e);
            request.setAttribute("errorMessage", "Failed to load dashboard data: " + e.getMessage());
        } catch (Exception e) {
            // Log any other exceptions
            LOGGER.log(Level.SEVERE, "Unexpected error while loading dashboard data", e);
            request.setAttribute("errorMessage", "An unexpected error occurred: " + e.getMessage());
        } finally {
            // Always set these attributes, even if they're empty
            request.setAttribute("recentContacts", recentContacts);
            request.setAttribute("genderStats", genderStats);
            request.setAttribute("countyStats", countyStats);

            // Forward to JSP
            RequestDispatcher dispatcher = request.getRequestDispatcher("/admin/dashboard.jsp");
            dispatcher.forward(request, response);
        }
    }
}

