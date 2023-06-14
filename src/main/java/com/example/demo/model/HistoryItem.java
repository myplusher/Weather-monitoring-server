package com.example.demo.model;

import com.google.gson.annotations.SerializedName;

public class HistoryItem {
    @SerializedName("date")
    private long date;

    @SerializedName("value")
    private String value;

    public HistoryItem(long date, String value) {
        this.date = date;
        this.value = value;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
