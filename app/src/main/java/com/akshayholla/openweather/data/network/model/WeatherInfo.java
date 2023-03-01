package com.akshayholla.openweather.data.network.model;

import com.google.gson.annotations.SerializedName;

public class WeatherInfo {
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getIconCode() {
        return iconCode;
    }

    @SerializedName("main")
    String title;
    @SerializedName("description")
    String description;
    @SerializedName("icon")
    String iconCode;
}