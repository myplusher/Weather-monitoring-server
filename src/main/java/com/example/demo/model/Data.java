package com.example.demo.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "data")
public class Data {
    private int id;
    private String title;
    private double temperature;
    private double humidity;
    private double co2;
    private double light;
    private Date date_time;

    public Data() {
    }

    public Data(int id, String title, double temperature, double humidity, double co2, double light, Date date_time) {
        this.id = id;
        this.title = title;
        this.temperature = temperature;
        this.humidity = humidity;
        this.co2 = co2;
        this.light = light;
        this.date_time = date_time;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
}
