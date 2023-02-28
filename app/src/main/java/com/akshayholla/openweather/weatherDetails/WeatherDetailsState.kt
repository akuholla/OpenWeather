package com.akshayholla.openweather.weatherDetails

import com.akshayholla.openweather.weatherDetails.model.WeatherViewData

sealed class WeatherDetailsState {
    object NetworkError : WeatherDetailsState()
    object CityNotFound : WeatherDetailsState()
    object LocationPermission : WeatherDetailsState()
    object Loading : WeatherDetailsState()
    data class WeatherData(val weatherData: WeatherViewData) : WeatherDetailsState()
}