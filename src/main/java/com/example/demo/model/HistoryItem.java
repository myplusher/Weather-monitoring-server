package com.example.demo.model;

import com.google.gson.annotations.SerializedName;

public class HistoryItem {
    @SerializedName("microcontroller")
    private long date;

    @SerializedName("microcontroller")
    private String value;

    public HistoryItem(long date, String value) {
        this.date = date;
        this.value = value;
    }
}
