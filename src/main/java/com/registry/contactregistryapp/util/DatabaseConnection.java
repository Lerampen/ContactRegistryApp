/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.registry.contactregistryapp.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Lera
 */
public class DatabaseConnection {
        private static final String JDBC_URL ="jdbc:mysql://localhost:3306/contact_registry_db";  
        private static final String JDBC_USER="root";  
        private static final String JDBC_PASSWORD ="root";  
        
    public static Connection getConnection() throws SQLException{
        try{
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        
        }catch(ClassNotFoundException e){
            throw  new SQLException("MySQL JDBC driver not found.", e);    
        }
    }
    
    public static void closeConnection(Connection connection){
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
       
}
    
    
    

