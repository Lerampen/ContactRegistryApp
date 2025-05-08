/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.registry.contactregistryapp.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Lera
 */
/**
 * Contact model class representing a contact in the registry
 */
public class Contact implements Serializable{

     private Long id;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String idNumber;
    private Date dateOfBirth;
    private String gender;
    private String countyOfResidence;
    private Date createdAt;
    private Date updatedAt;
    
      // Default constructor
    public Contact() {
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }
    
//    Constructor with parameters
      public Contact( String fullName, String phoneNumber, String email, String idNumber, Date dateOfBirth, String gender, String countyOfResidence, Date createdAt, Date updatedAt) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.idNumber = idNumber;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.countyOfResidence = countyOfResidence;
        this.createdAt = new Date();
        this.updatedAt =  new Date();
    }
        // Getters and Setters

         public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCountyOfResidence() {
        return countyOfResidence;
    }

    public void setCountyOfResidence(String countyOfResidence) {
        this.countyOfResidence = countyOfResidence;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Contact{" + "id=" + id + ", fullName=" + fullName + ", phoneNumber=" + phoneNumber + ", email=" + email + ", idNumber=" + idNumber + ", dateOfBirth=" + dateOfBirth + ", gender=" + gender + ", countyOfResidence=" + countyOfResidence + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + '}';
    }
    
    
}
