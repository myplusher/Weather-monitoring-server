package com.example.demo.model;

import com.google.gson.annotations.SerializedName;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "data")
public class Data {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String title; // Класть название помещения
    private double temperature;
    private double humidity;
    private double co2;
    private double light;
    private Date date_time;

    @Transient
    private String temperatureRange;
    @Transient
    private String humidityRange;
    @Transient
    private String co2Range;
    @Transient
    private String lightRange;

    @SerializedName("microcontroller")
    @ManyToOne
    @JoinColumn(name = "microcontrollerId", insertable = true, updatable = true)
    private Microcontroller microcontrollerId;

    public Microcontroller getMicrocontroller() {
        return microcontrollerId;
    }

    public void setMicrocontroller(Microcontroller microcontroller) {
        this.microcontrollerId = microcontroller;
    }

    public Data() {
    }

    public Data(int id, String title, double temperature, double humidity, double co2, double light, Date date_time,
                Microcontroller microcontroller) {
        this.id = id;
        this.title = title;
        this.temperature = temperature;
        this.humidity = humidity;
        this.co2 = co2;
        this.light = light;
        this.date_time = date_time;
        this.microcontrollerId = microcontroller;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getCo2() {
        return co2;
    }

    public void setCo2(double co2) {
        this.co2 = co2;
    }

    public double getLight() {
        return light;
    }

    public void setLight(double light) {
        this.light = light;
    }

    public Date getDate_time() {
        return date_time;
    }

    public void setDate_time(Date date_time) {
        this.date_time = date_time;
    }

    public String getTemperatureRange() {
        return temperatureRange;
    }

    public void setTemperatureRange(String temperatureRange) {
        this.temperatureRange = temperatureRange;
    }

    public String getHumidityRange() {
        return humidityRange;
    }

    public void setHumidityRange(String humidityRange) {
        this.humidityRange = humidityRange;
    }

    public String getCo2Range() {
        return co2Range;
    }

    public void setCo2Range(String co2Range) {
        this.co2Range = co2Range;
    }

    public String getLightRange() {
        return lightRange;
    }

    public void setLightRange(String lightRange) {
        this.lightRange = lightRange;
    }
}
