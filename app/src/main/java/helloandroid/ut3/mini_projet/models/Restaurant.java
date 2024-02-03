package helloandroid.ut3.mini_projet.models;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

public class Restaurant {

    String id;
    String nom;
    String address;
    String[] photos;
    GeoPoint coordinates;
    String description;
    String horaire;
    String type;
    float note;


    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getAddress() {
        return address;
    }

    public String[] getPhotos() {
        return photos;
    }

    public org.osmdroid.util.GeoPoint getCoordinates() {
        return new org.osmdroid.util.GeoPoint(coordinates.getLatitude(),coordinates.getLongitude());
    }

    public String getDescription() {
        return description;
    }

    public String getHoraire() {
        return horaire;
    }

    public String getType() {
        return type;
    }

    public float getNote() {
        return note;
    }

    public Restaurant(String id, String nom, String address, String[] photos, GeoPoint coordinates, String description, String horaire, String type, float note) {
        this.id = id;
        this.nom = nom;
        this.address = address;
        this.photos = photos;
        this.coordinates = coordinates;
        this.description = description;
        this.horaire = horaire;
        this.type = type;
        this.note = note;
    }
}
