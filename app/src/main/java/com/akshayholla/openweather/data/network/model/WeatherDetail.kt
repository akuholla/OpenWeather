package com.akshayholla.openweather.data.network.model

import com.google.gson.annotations.SerializedName

data class WeatherDetail(
    @SerializedName("name") val cityName: String,
    @SerializedName("main") val temperatureData: TemperatureData,
    @SerializedName("id") val id: Int,
    @SerializedName("coord") val coordinates: Coordinates,
    @SerializedName("weather") val weatherInfo: List<WeatherInfo>,
    @SerializedName("wind") val windinfo: WindInfo,
    @SerializedName("sys") val sys: Sys,
)

data class TemperatureData(
    @SerializedName("temp") val temperature: Float,
    @SerializedName("feels_like") val feelsLike: Float,
    @SerializedName("temp_min") val minTemp: Float,
    @SerializedName("temp_max") val maxTemp: Float,
    @SerializedName("pressure") val pressure: Float,
    @SerializedName("humidity") val humidity: Float,
)

data class Coordinates(
    val lat: Double,
    val lon: Double,
)

data class WeatherInfo(
    @SerializedName("main") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("icon") val iconCode: String,
)

data class WindInfo(
    @SerializedName("speed") val speed: Float,
    @SerializedName("deg") val angle: Float,
)

data class Sys(
    @SerializedName("country") val countryCode: String,
    @SerializedName("sunrise") val sunrise: String,
    @SerializedName("sunset") val sunset: String,
)

