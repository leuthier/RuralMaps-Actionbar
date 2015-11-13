package com.mpoo.ruralmaps.ruralmaps;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class Placemark implements Serializable{
    private static final long serialVersionUID = 1L;
    private String name;
    private String description;
    private double latitude;
    private double longitude;
    private String iconID;

    public Placemark(String name, String description, LatLng coordinates, String iconID) {
        this(name,description,iconID,coordinates.latitude,coordinates.longitude);
    }

    public Placemark(String name, String description, String iconID, double latitude, double longitude){
        this.name = name;
        this.description = description;
        this.iconID = iconID;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public Placemark(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LatLng getCoordinates() {
        return new LatLng(latitude, longitude);
    }

    public void setCoordinates(LatLng coordinates) {
        latitude = coordinates.latitude;
        longitude = coordinates.longitude;
    }

    public void setIconID(String iconID){
        this.iconID = iconID;
    }

    public String getIconID(){
        return iconID;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        return o.toString().equals(toString());
    }
}