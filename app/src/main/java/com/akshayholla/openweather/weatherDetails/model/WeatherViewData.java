package com.akshayholla.openweather.weatherDetails.model;

public class WeatherViewData {
    public String cityName;
    public String temperature;
    public String feelsLike;
    public String minTemp;
    public String maxTemp;
    public String title;

    public String getCityName() {
        return cityName;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getFeelsLike() {
        return feelsLike;
    }

    public String getMinTemp() {
        return minTemp;
    }

    public String getMaxTemp() {
        return maxTemp;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }

    public String getPressure() {
        return pressure;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public String getSunrise() {
        return sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public String description;
    public String icon;

    public WeatherViewData(String cityName, String temperature, String feelsLike, String minTemp, String maxTemp, String title, String description, String icon, String pressure, String humidity, String windSpeed, String windDirection, String sunrise, String sunset) {
        this.cityName = cityName;
        this.temperature = temperature;
        this.feelsLike = feelsLike;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.title = title;
        this.description = description;
        this.icon = icon;
        this.pressure = pressure;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
        this.sunrise = sunrise;
        this.sunset = sunset;
    }

    public String pressure;
    public String humidity;
    public String windSpeed;
    public String windDirection;
    public String sunrise;
    public String sunset;
}
