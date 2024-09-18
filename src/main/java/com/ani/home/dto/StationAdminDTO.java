package com.ani.home.dto;

import java.math.BigDecimal;

public class StationAdminDTO {

    private int id;
    private String name;
    private String email;
    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private double distance; // Distance field for frontend use

    // Constructors, getters, and setters
    public StationAdminDTO() {
    }

    public StationAdminDTO(int id, String name, String email, String address, BigDecimal latitude, BigDecimal longitude, double distance) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
//package com.ani.home.dto;
//
//import java.math.BigDecimal;
//
//public class StationAdminDTO {
//
//    private int id;
//    private String charging_station_name;
//    private String email;
//    private String address;
//    private BigDecimal latitude;
//    private BigDecimal longitude;
//    private double distance; // Distance field for frontend use
//
//    // Constructors, getters, and setters
//    public StationAdminDTO() {
//    }
//
//
//    public String getCharging_station_name() {
//		return charging_station_name;
//	}
//
//
//	public void setCharging_station_name(String charging_station_name) {
//		this.charging_station_name = charging_station_name;
//	}
//
//
//	public StationAdminDTO(int id, String charging_station_name, String email, String address, BigDecimal latitude,
//			BigDecimal longitude, double distance) {
//		super();
//		this.id = id;
//		this.charging_station_name = charging_station_name;
//		this.email = email;
//		this.address = address;
//		this.latitude = latitude;
//		this.longitude = longitude;
//		this.distance = distance;
//	}
//
//
//	// Getters and setters
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//  
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getAddress() {
//        return address;
//    }
//
//    public void setAddress(String address) {
//        this.address = address;
//    }
//
//    public BigDecimal getLatitude() {
//        return latitude;
//    }
//
//    public void setLatitude(BigDecimal latitude) {
//        this.latitude = latitude;
//    }
//
//    public BigDecimal getLongitude() {
//        return longitude;
//    }
//
//    public void setLongitude(BigDecimal longitude) {
//        this.longitude = longitude;
//    }
//
//    public double getDistance() {
//        return distance;
//    }
//
//    public void setDistance(double distance) {
//        this.distance = distance;
//    }
//}
//
