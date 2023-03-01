package com.akshayholla.openweather.data.network.model;

import com.google.gson.annotations.SerializedName;

public class Sys {
    public String getCountryCode() {
        return countryCode;
    }

    public String getSunrise() {
        return sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    @SerializedName("country")
    String countryCode;
    @SerializedName("sunrise")
    String sunrise;
    @SerializedName("sunset")
    String sunset;
}