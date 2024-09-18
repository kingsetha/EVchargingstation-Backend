
package com.ani.home.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "station_admin")
public class StationAdmin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false)
    private String password;

    public StationAdmin() {
		super();
		 this.totalDevice = 3;
	}

	@Column(length = 50)
    private String phoneNumber;

    @Column(length = 255)
    private String address;

    @Column(nullable = false, length = 50)
    private String role = "station_admin";

    @Column(nullable = false)
    private Boolean isActive = true;

    @Column(nullable = false)
    private Boolean isBlocked = false;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "charging_station_name", length = 255)
    private String chargingStationName;

    @Column(name = "charging_station_location", length = 255)
    private String chargingStationLocation;

    @Column(name = "charging_station_address", length = 255)
    private String chargingStationAddress;

    @Column(name = "latitude", precision = 9, scale = 6)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 9, scale = 6)
    private BigDecimal longitude;

    @Column(name = "total_device")
    private Integer totalDevice =3;

    @Column(name = "image_url", length = 1000)
    private String imageUrl;

    @Column(name = "registration_certificate_number", length = 255)
    private String registrationCertificateNumber; // New field

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(Boolean isBlocked) {
        this.isBlocked = isBlocked;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getChargingStationName() {
        return chargingStationName;
    }

    public void setChargingStationName(String chargingStationName) {
        this.chargingStationName = chargingStationName;
    }

    public String getChargingStationLocation() {
        return chargingStationLocation;
    }

    public void setChargingStationLocation(String chargingStationLocation) {
        this.chargingStationLocation = chargingStationLocation;
    }

    public String getChargingStationAddress() {
        return chargingStationAddress;
    }

    public void setChargingStationAddress(String chargingStationAddress) {
        this.chargingStationAddress = chargingStationAddress;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public Integer getTotalDevice() {
        return totalDevice;
    }

    public void setTotalDevice(Integer totalDevice) {
        this.totalDevice = totalDevice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getRegistrationCertificateNumber() {
        return registrationCertificateNumber;
    }

    public void setRegistrationCertificateNumber(String registrationCertificateNumber) {
        this.registrationCertificateNumber = registrationCertificateNumber;
    }

    public enum Status {
        PENDING,
        APPROVED,
        REJECTED
    }
}

