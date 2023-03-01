package com.akshayholla.openweather.data.network.model;

import com.google.gson.annotations.SerializedName;

public class TemperatureData {
    public float getTemperature() {
        return temperature;
    }

    public float getFeelsLike() {
        return feelsLike;
    }

    public float getMinTemp() {
        return minTemp;
    }

    public float getMaxTemp() {
        return maxTemp;
    }

    public float getPressure() {
        return pressure;
    }

    public float getHumidity() {
        return humidity;
    }

    @SerializedName("temp")
    float temperature;
    @SerializedName("feels_like")
    float feelsLike;
    @SerializedName("temp_min")
    float minTemp;
    @SerializedName("temp_max")
    float maxTemp;
    @SerializedName("pressure")
    float pressure;
    @SerializedName("humidity")
    float humidity;
}