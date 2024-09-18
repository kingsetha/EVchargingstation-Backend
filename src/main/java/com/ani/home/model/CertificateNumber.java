package com.ani.home.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "certificate_numbers")
public class CertificateNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String certificateNumber;
    private boolean active; // Field to indicate whether the certificate is active

    // Default constructor
    public CertificateNumber() {
        this.active = false; // Default to inactive
    }

    // Parameterized constructor
    public CertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
        this.active = false; // Default to inactive
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
