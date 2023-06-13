package com.example.demo.model;

import java.util.List;

public class RoomHistoryDto {
    private List<HistoryItem> temperatureHistory;
    private List<HistoryItem> humidityHistory;
    private List<HistoryItem> co2History;
    private List<HistoryItem> lightHistory;

    public RoomHistoryDto() {}

    public RoomHistoryDto(List<HistoryItem> temperatureHistory, List<HistoryItem> humidityHistory, List<HistoryItem> co2History, List<HistoryItem> lightHistory) {
        this.temperatureHistory = temperatureHistory;
        this.humidityHistory = humidityHistory;
        this.co2History = co2History;
        this.lightHistory = lightHistory;
    }

    public List<HistoryItem> getTemperatureHistory() {
        return temperatureHistory;
    }

    public void setTemperatureHistory(List<HistoryItem> temperatureHistory) {
        this.temperatureHistory = temperatureHistory;
    }

    public List<HistoryItem> getHumidityHistory() {
        return humidityHistory;
    }

    public void setHumidityHistory(List<HistoryItem> humidityHistory) {
        this.humidityHistory = humidityHistory;
    }

    public List<HistoryItem> getCo2History() {
        return co2History;
    }

    public void setCo2History(List<HistoryItem> co2History) {
        this.co2History = co2History;
    }

    public List<HistoryItem> getLightHistory() {
        return lightHistory;
    }

    public void setLightHistory(List<HistoryItem> lightHistory) {
        this.lightHistory = lightHistory;
    }
}
