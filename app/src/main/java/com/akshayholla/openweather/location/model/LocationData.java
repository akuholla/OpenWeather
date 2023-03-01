package com.akshayholla.openweather.location.model;

public class LocationData {
    public LocationData(double lat, double lon) {
        this.latitude = lat;
        this.longitude = lon;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    double latitude;
    double longitude;

}
