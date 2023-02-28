package com.akshayholla.openweather.data.network

import com.akshayholla.openweather.common.api25
import com.akshayholla.openweather.data.network.model.WeatherDetail
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApi {
    @GET("/${api25}/weather")
    suspend fun getWeatherByCoordinates(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String,
        @Query("appid") appid: String,
    ): Response<WeatherDetail>

    @GET("/${api25}/weather")
    suspend fun getWeatherByCityName(
        @Query("q") name: String,
        @Query("units") units: String,
        @Query("appid") appid: String,
    ): Response<WeatherDetail>
}