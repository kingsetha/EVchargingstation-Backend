package com.ani.home.model;

public class LocationData {
    private Location currentLocation;
    private Location stationLocation;

    // Constructors
    public LocationData() {}

    public LocationData(Location currentLocation, Location stationLocation) {
        this.currentLocation = currentLocation;
        this.stationLocation = stationLocation;
    }

    // Getters and Setters
    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public Location getStationLocation() {
        return stationLocation;
    }

    public void setStationLocation(Location stationLocation) {
        this.stationLocation = stationLocation;
    }
}