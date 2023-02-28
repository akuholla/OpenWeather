package com.akshayholla.openweather.data.network

import com.akshayholla.openweather.common.ApiEnabler
import com.akshayholla.openweather.data.network.model.WeatherDetail
import retrofit2.Response
import javax.inject.Inject

class OpenWeatherServiceProvider @Inject constructor(
    private val openWeatherApi: OpenWeatherApi,
    private val apiEnabler: ApiEnabler,
) {
    suspend fun getWeatherByCoordinates(
        latitude: Double,
        longitude: Double
    ): Response<WeatherDetail> {
        return openWeatherApi.getWeatherByCoordinates(
            latitude = latitude,
            longitude = longitude,
            units = "metric",
            //TODO: auto fetch units = "imperial",
            appid = apiEnabler.getWeatherApiKey()
        )
    }

    suspend fun getWeatherByCity(name: String): Response<WeatherDetail> {
        return openWeatherApi.getWeatherByCityName(
            name = name,
            units = "metric",
            appid = apiEnabler.getWeatherApiKey()
        )
    }
}