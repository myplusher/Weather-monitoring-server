package com.example.demo.model;

import com.google.gson.annotations.SerializedName;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Microcontroller {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String address;
    @SerializedName("short_name")
    private String shortName;

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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
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
