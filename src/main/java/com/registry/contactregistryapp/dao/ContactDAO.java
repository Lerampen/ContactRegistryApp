/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.registry.contactregistryapp.dao;

import com.registry.contactregistryapp.model.Contact;
import com.registry.contactregistryapp.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Lera
 */
public class ContactDAO {
    
    /**
     * Create a new contact in the database 
     * @param contact The contact to create
     * @return the created contact with ID populated
     * @throws SQLException if database operation fails
     */
    public Contact create(  Contact contact) throws SQLException {
        String sql = "INSERT INTO contacts(full_name, phone, email, id_number, date_of_birth,gender, county_residence, created_at, updated_at)"
                + "VALUES(?,?,?,?,?,?,?, NOW(), NOW())";
        try(Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
            statement.setString(1, contact.getFullName());
            statement.setString(2, contact.getPhoneNumber());
            statement.setString(3, contact.getEmail());
            statement.setString(4, contact.getIdNumber());
            statement.setDate(5, new java.sql.Date(contact.getDateOfBirth().getTime()));
            statement.setString(6, contact.getGender());
            statement.setString(7, contact.getCountyOfResidence());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows > 0) {
                try(ResultSet generatedkeys = statement.getGeneratedKeys()) {
                    if(generatedkeys.next()) {
                        contact.setId(generatedkeys.getLong(1));
                    }
                } 
            }else{
             throw new SQLException("Creating contact failed,");
        }  
    }
     return contact;         

    }
    
    /**
     * Retrieve a contact by ID
     * @param id The contact ID
     * @return The contact or null if not found
     * @throws SQLException 
     */
    public Contact findById(Long id) throws SQLException{
        String sql = "SELECT * FROM contacts WHERE id = ?"; 
        try(Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement =  connection.prepareStatement(sql)) {
           statement.setLong(1, id);
           try(ResultSet resultSet = statement.executeQuery()){
               if (resultSet.next()) {
                   return mapResultSetToContact(resultSet);
               }
               return null;
           }
        } 
    }
    
    /**
     * Retreive all contacts in the database
     * @return list of all contacts
     * @throws SQLException if database operation fails
     */
    public List<Contact> findAll() throws SQLException{
        String sql = "SELECT * FROM contacts";
        List<Contact> contacts = new ArrayList<>();
        
         Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
        
         try {
        connection = DatabaseConnection.getConnection();
        System.out.println("DEBUG (findAll): Database connection successful.");
        statement = connection.prepareStatement(sql);
        System.out.println("DEBUG (findAll): Prepared statement created: " + sql);
        resultSet = statement.executeQuery();
        System.out.println("DEBUG (findAll): Query executed.");

        while (resultSet.next()) {
            System.out.println("DEBUG (findAll): Row found - ID: " + resultSet.getLong("id") + ", Name: " + resultSet.getString("full_name"));
            contacts.add(mapResultSetToContact(resultSet));
        }
        System.out.println("DEBUG (findAll): Found " + contacts.size() + " contacts.");
    } catch (SQLException e) {
        System.err.println("ERROR (findAll): " + e.getMessage());
        throw e;
    } finally {
        // Ensure resources are closed in a finally block
        if (resultSet != null) try { resultSet.close(); } catch (SQLException e) {}
        if (statement != null) try { statement.close(); } catch (SQLException e) {}
        if (connection != null) try { connection.close(); } catch (SQLException e) {}
    }
        return contacts;
    }
    
    /**
     * Update an existing contact
     * @param contact The contact to update with new data
     * @return Updated contact 
     * @throws SQLException 
     */
    public Contact update(Contact contact) throws  SQLException{
        String sql = "UPDATE contacts SET full_name = ?, phone = ?, email = ?, id_number = ?, date_of_birth = ?, gender = ?, county_residence = ?, updated_at = ? WHERE id = ?";
        try(Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, contact.getFullName());
            statement.setString(2, contact.getPhoneNumber());
            statement.setString(3, contact.getEmail());
            statement.setString(4, contact.getIdNumber());
            statement.setDate(5, new java.sql.Date(contact.getDateOfBirth().getTime()));
            statement.setString(6, contact.getGender());
            statement.setString(7, contact.getCountyOfResidence());
            statement.setTimestamp(8, new Timestamp(new Date().getTime()));
            statement.setLong(9, contact.getId());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows > 0) {
                return  findById(contact.getId());
            }else{
                throw new SQLException("Updating contact failed, no rows affected for id: " + contact.getId());
            }
            
        }
    }
    
    /**
     * Delete a contact by ID
     * @param id The ID of the contact to delete
     * @return true if deleted successfully, false otherwise
     * @throws SQLException if database operation fails
     */
    public boolean delete(Long id)throws SQLException{
        String sql = "DELETE FROM contacts   WHERE id = ?";
        
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            
            statement.setLong(1, id);
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        }
        
    }
    /**
     * Get 5 most recently added Contacts
     * @return List of 5 recent contacts
     * @throws SQLException if database operation  fails
     */
    public List<Contact> findRecent() throws SQLException{
        List<Contact> contacts = new ArrayList<>();
        String sql = "SELECT * FROM contacts ORDER BY created_at DESC LIMIT 5";
        try(Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()) {
                contacts.add(mapResultSetToContact(rs));
            }
        }
        return contacts;
    }
    /**
     * Get gender distribution statistics
     * @return  Map of gender to count
     * @throws SQLException if database operation fails
    */
    
    public  Map<String, Integer> getGenderStats() throws SQLException{
       Map<String, Integer> stats = new HashMap<>();
       String sql = "SELECT gender, COUNT(*) as count FROM contacts GROUP BY gender";
       
       try(Connection conn = DatabaseConnection.getConnection();
               Statement stmt = conn.createStatement();
               ResultSet rs = stmt.executeQuery(sql)){
           
           while (rs.next()) {               
               stats.put(rs.getString("gender"), rs.getInt("count"));
           }
       }
       return stats;
    }
    
    /**
     * Get county distribution statistics
     * @return Map of county to count
     * @throws SQLException if database operation fails
     */
    public Map<String, Integer> getCountyStats() throws SQLException{
        Map<String, Integer> stats = new HashMap<>();
        String sql = "SELECT county_residence, COUNT(*) as count FROM contacts GROUP BY county_residence";
        try(Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()) {                
                stats.put(rs.getString("county_residence"), rs.getInt("count"));
            }
        }
        return  stats;
    }
    // In ContactDAO.java

/**
 * Retrieve contacts by their county of residence.
 * @param county The county to filter by.
 * @return A list of contacts residing in the specified county.
 * @throws SQLException if a database error occurs.
 */
public List<Contact> findByCounty(String county) throws SQLException {
    String sql = "SELECT * FROM contacts WHERE county_residence = ?";
    List<Contact> contacts = new ArrayList<>();

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
        statement.setString(1, county);
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                contacts.add(mapResultSetToContact(resultSet));
            }
        }
    }
    return contacts;
}
    /**
     * Get unique counties from database
     * @return list of county names
     * @throws SQLException If database operation fails
     */
    public List<String> getAllCounties() throws SQLException{
        List<String> counties = new ArrayList<>();
        String sql = "SELECT DISTINCT county_residence FROM contacts ORDER BY county_residence";
        
        try(Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()) {                
                counties.add(rs.getString("county_residence"));
            }
        }return counties;
    }
    
/**
 * Helper method to map Result to Contact object
 * @param resultSet from database query
 * @return Contact object with data from ResultSet
 * @throws SQLException  if database operation fails
 */
    private Contact mapResultSetToContact(ResultSet resultSet)throws SQLException{
        Contact contact = new Contact();
        contact.setId(resultSet.getLong("id"));
        contact.setFullName(resultSet.getString("full_name"));
        contact.setPhoneNumber(resultSet.getString("phone"));
        contact.setEmail(resultSet.getString("email"));
        contact.setIdNumber(resultSet.getString("id_number"));
        contact.setDateOfBirth(resultSet.getDate("date_of_birth"));
        contact.setGender(resultSet.getString("gender"));
        contact.setCountyOfResidence(resultSet.getString("county_residence"));
        contact.setCreatedAt(resultSet.getTimestamp("created_at"));
        contact.setUpdatedAt(resultSet.getTimestamp("updated_at"));
        return contact;
        
    }
}

