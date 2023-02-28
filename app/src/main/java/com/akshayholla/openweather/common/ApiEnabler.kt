package com.akshayholla.openweather.common

import com.akshayholla.openweather.BuildConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiEnabler @Inject constructor() {
    fun getWeatherApiKey() = BuildConfig.OPEN_WEATHER_API_KEY
}