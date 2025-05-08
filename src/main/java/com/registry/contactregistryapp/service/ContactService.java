/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.registry.contactregistryapp.service;

import com.registry.contactregistryapp.dao.ContactDAO;
import com.registry.contactregistryapp.model.Contact;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 *
 * @author Lera
 */
public class ContactService {
    
    private  final ContactDAO contactDAO = new ContactDAO();
    
//    Email validation pattern
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
                "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$"
);
     // Phone number validation pattern 
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9]{10,15}$");
    //ID Number Pattern
    private static final Pattern ID_PATTERN = Pattern.compile("^[0-9]{8}$");        
    
    public Contact createContact(Contact contact) throws Exception{
//     Validation logic(e.g check for required fields, valid email etc,)
        if (contact.getFullName() == null || contact.getFullName().trim().isEmpty()) {
            throw new IllegalArgumentException("Full name is required");
        }
        if (contact.getPhoneNumber() == null || contact.getPhoneNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number is required");
        }
          if (!PHONE_PATTERN.matcher(contact.getPhoneNumber()).matches()) {
            throw new IllegalArgumentException("Invalid phone number format");
          }
        // validate the email appropiately
        if (contact.getEmail()== null || contact.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
          if (!EMAIL_PATTERN.matcher(contact.getEmail()).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (contact.getIdNumber() == null || contact.getIdNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("ID Number is required");
        }
         if (!ID_PATTERN.matcher(contact.getIdNumber()).matches()) {
            throw new IllegalArgumentException("Invalid Kenyan ID format. Must be 8 digits.");
        }
//        validate the date of Birth
        if (contact.getDateOfBirth() != null  ) {
            try {
                Date dobDate = contact.getDateOfBirth();
                LocalDate dob =dobDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate now = LocalDate.now();
                
//                Check if date is in the future
                if (dob.isAfter(now)) {
                    throw new IllegalArgumentException("Date of birth cannot be in the future");
                }
//                Check if age is reasonable e.g less than 120 years
                int age = Period.between(dob, now).getYears();
                if (age > 120) {
                    throw new IllegalArgumentException("Invalid date of birth: Age exceeds 120 years");
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid date of birth format", e);
            }
        }
        
      
        if (contact.getGender() == null || contact.getGender().trim().isEmpty()) {
            throw new IllegalArgumentException("Gender is required");
        }
        if (!contact.getGender().equalsIgnoreCase("male") && !contact.getGender().equalsIgnoreCase("female")) {
            throw new IllegalArgumentException("Invalid Gender. Must be 'Male', 'Female'");
 
        }
        if (contact.getCountyOfResidence()== null || contact.getCountyOfResidence().trim().isEmpty()) {
            throw new IllegalArgumentException("County of residence is required");
        }
        
        try {
            return contactDAO.create(contact);
        } catch (SQLException e) {
            throw new Exception("Error creating contact", e);
               
        }
    }
/**
 * Retreives a contact theby its ID
 * @param id the ID of the contact to retrieve
 * @return The retreived contact
 * @throws Exception if an error occurs during retrieval
 */   
    public  Contact getContactById( Long id) throws  Exception{
        if (id == null) {
            throw new IllegalArgumentException("Contact ID cannot be null");
        }
        try {
            return  contactDAO.findById(id);
        } catch (SQLException e) {
            throw  new Exception("Error retrieving contact",e);
        }
    }
    
    public List<Contact> getAllContacts() throws  Exception{
        try {
            return  contactDAO.findAll();
        } catch (SQLException e) {
        throw new Exception("Error retrieving all contacts",e);
        }
    }
    
    public Contact updateContact(Contact contact) throws Exception{
        // Validation logic
        if(contact.getId() == null){
            throw new IllegalArgumentException("Contact ID is required for updating.");
        }
         
        //  same validation logic as in createContact
        if (contact.getFullName() == null || contact.getFullName().trim().isEmpty()) {
            throw new IllegalArgumentException("Full name is required");
        }
        
        if (contact.getPhoneNumber() == null || contact.getPhoneNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number is required");
        }
        if (!PHONE_PATTERN.matcher(contact.getPhoneNumber()).matches()) {
            throw new IllegalArgumentException("Invalid phone number format");
        }
        
        if (contact.getEmail() == null || contact.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (!EMAIL_PATTERN.matcher(contact.getEmail()).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }
        
        if (contact.getIdNumber() == null || contact.getIdNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("ID number is required");
        }
        if (!ID_PATTERN.matcher(contact.getIdNumber()).matches()) {
            throw new IllegalArgumentException("Invalid Kenyan ID format. Must be 8 digits.");
        }
        
        // Validate date of birth if provided
        if (contact.getDateOfBirth() != null) {
            try {
                // Convert java.util.Date to LocalDate
                Date dobDate = contact.getDateOfBirth();
                LocalDate dob = dobDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate now = LocalDate.now();
                
                if (dob.isAfter(now)) {
                    throw new IllegalArgumentException("Date of birth cannot be in the future");
                }
                
                int age = Period.between(dob, now).getYears();
                if (age > 120) {
                    throw new IllegalArgumentException("Invalid date of birth: Age exceeds 120 years");
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid date of birth format", e);
            }
        }
        
        if (contact.getGender() == null || contact.getGender().trim().isEmpty()) {
            throw new IllegalArgumentException("Gender is required");
        }
        if (!contact.getGender().equalsIgnoreCase("male") && !contact.getGender().equalsIgnoreCase("female")) {
            throw new IllegalArgumentException("Invalid Gender. Must be 'Male' or 'Female'");
        }
        
        if (contact.getCountyOfResidence() == null || contact.getCountyOfResidence().trim().isEmpty()) {
            throw new IllegalArgumentException("County of residence is required");
        }
        try {
            return contactDAO.update(contact);
        } catch (SQLException e) {
            throw new Exception("Error updating contact",e);
        }
    }
    
    public boolean deleteContact(Long id) throws Exception{
        if(id == null){
            throw new Exception("Contact ID cannot be null");
        }
        try {
            return contactDAO.delete(id);
        } catch (SQLException e) {
        throw new Exception("Error deleting contact", e);
        }
        
    }
//    Method to get gender statistics for the dashboard
    public Map<String, Integer> getGenderStats() throws Exception{
       List<Contact> allContacts = getAllContacts();
        Map<String, Integer> genderStats = new HashMap<>();
        genderStats.put("Male", 0);
        genderStats.put("Female", 0);
        
        for (Contact contact : allContacts) {
            if (contact.getGender() != null) { // Handle null gender values
                String gender = contact.getGender().trim().toLowerCase();
                if (gender.equals("male")){
                    genderStats.put("Male", genderStats.get("Male") + 1);
                }else if (gender.equals("female")){
                    genderStats.put("Female", genderStats.get("Female") + 1);
                }
            }
            
        }
        return genderStats;
    }
    
//   Method to get county statistics for the dashboard
    public Map<String, Integer> getCountyStats() throws Exception{
        List<Contact> allContacts = getAllContacts();
        Map<String, Integer> countyStats = new HashMap<>();
        for (Contact contact : allContacts) {
            if (contact.getCountyOfResidence() != null) {
                String county = contact.getCountyOfResidence().trim();
                countyStats.put(county, countyStats.getOrDefault(county, 0)+ 1);
            }
        }
        
        return countyStats;
    }

//    Method to get recently added contacts for the dashboard
    public List<Contact> getRecentContacts(int count) throws Exception{
        if (count <= 0) {
            throw new  IllegalArgumentException("Count must be grater than zero");
        }
        try{
           List<Contact> allContacts = contactDAO.findAll(); // Get all contacts
        allContacts.sort((c1,c2) -> c2.getCreatedAt().compareTo(c1.getCreatedAt()));
        
        if (allContacts.size() > count) {
            return  allContacts.subList(0, count); // return the top 'count'
        }else{
            return allContacts; // Return all if there are fewer than 'count'
        
        }
       }catch (SQLException e){
           throw new Exception("Error Retrieving recent contacts", e );
       }
    }
    
    
}
