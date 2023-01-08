package com.example.demo.model;

import com.google.gson.annotations.SerializedName;

import javax.persistence.*;

@Entity
public class Microcontroller {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String address;
    @SerializedName("shortname")
    private String shortname;

    @SerializedName("location_id")
    private int locationID;

    @SerializedName("location_name")
    @Transient
    private String locationName;

    public Microcontroller() {}

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getId() {
        return id;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortName) {
        this.shortname = shortName;
    }

    public int getLocationID() {
        return locationID;
    }

    public void setLocationID(int locationID) {
        this.locationID = locationID;
    }

    public String getLocation() {
        return locationName;
    }

    public void setLocation(String locationName) {
        this.locationName = locationName;
    }
}
