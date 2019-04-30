package com.example.like.miniprojetandroid;


/**
 * Created by pc on 23/03/2019.
 */

public class Place {
    int id;
    double lat;
    double lng;
    String nom;

    public Place(){}


    public Place(int id, double lat, double lng, String nom) {
        this.id = id;
        this.lat = lat;
        this.lng = lng;
        this.nom = nom;
    }


    public int getId() {
        return id;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getNom() {
        return nom;
    }
}
