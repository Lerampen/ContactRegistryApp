/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.registry.contactregistryapp.servlet;

import com.registry.contactregistryapp.dao.ContactDAO;
import com.registry.contactregistryapp.util.DatabaseConnection;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lera
 */
public class ContactReportServlet extends HttpServlet {

  private static final Logger LOGGER = Logger.getLogger(ContactReportServlet.class.getName());
    private ContactDAO contactDAO;
    
      @Override
    public void init() throws ServletException {
        super.init();
        contactDAO = new ContactDAO();
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
        String county = request.getParameter("county");
        if (county == null || county.isEmpty()) {
            county = "All Counties"; // Default value if no county is selected.
        }

        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();

            // 1. Load the JasperReport design file (.jrxml).
            InputStream reportStream = getServletContext().getResourceAsStream("/admin/contactreport.jrxml"); // Path to your .jrxml
            if (reportStream == null) {
                LOGGER.severe("ERROR: JasperReport file not found!");
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Report file not found.");
                return;
            }
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            // 2. Set the report parameters.
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("CountyFilter", county);  // Pass the selected county

            // 3. Fill the report with data from the database.
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

            // 4. Export the report to PDF.
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));
            SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
            exporter.setConfiguration(configuration);
            exporter.exportReport();

            // 5. Set the response headers for the PDF.
            response.setContentType("application/pdf");
            response.setContentLength(baos.size());
            response.setHeader("Content-Disposition", "inline; filename=\"contacts_report.pdf\""); // or "attachment; ..." for download

            // 6. Send the PDF data to the client.
            response.getOutputStream().write(baos.toByteArray());
            response.getOutputStream().flush();
            response.getOutputStream().close();

        } catch (JRException | SQLException e) {
            LOGGER.log(Level.SEVERE, "Error generating report: ", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to generate report: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    LOGGER.log(Level.WARNING, "Error closing database connection: ", ex);
                }
            }
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

}
