package helloandroid.ut3.mini_projet.models;

import java.io.Serializable;

public class CustomGeoPoint implements Serializable {
    private double latitude;
    private double longitude;

    public CustomGeoPoint(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
