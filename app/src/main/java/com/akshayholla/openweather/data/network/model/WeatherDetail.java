package com.akshayholla.openweather.data.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherDetail {
    public String getCityName() {
        return cityName;
    }

    public TemperatureData getTemperatureData() {
        return temperatureData;
    }

    public int getId() {
        return id;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public List<WeatherInfo> getWeatherInfo() {
        return weatherInfo;
    }

    public WindInfo getWindinfo() {
        return windinfo;
    }

    public Sys getSys() {
        return sys;
    }

    @SerializedName("name")
    String cityName;
    @SerializedName("main")
    TemperatureData temperatureData;
    @SerializedName("id")
    int id;
    @SerializedName("coord")
    Coordinates coordinates;
    @SerializedName("weather")
    List<WeatherInfo> weatherInfo;
    @SerializedName("wind")
    WindInfo windinfo;
    @SerializedName("sys")
    Sys sys;


}
