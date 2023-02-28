package com.akshayholla.openweather.repository

import android.util.Log
import com.akshayholla.openweather.common.DateUtil
import com.akshayholla.openweather.common.ErrorType
import com.akshayholla.openweather.common.Response
import com.akshayholla.openweather.data.SharedPrefDataSource
import com.akshayholla.openweather.data.network.OpenWeatherServiceProvider
import com.akshayholla.openweather.data.network.model.WeatherDetail
import com.akshayholla.openweather.location.LocationService
import com.akshayholla.openweather.location.model.LocationData
import com.akshayholla.openweather.weatherDetails.model.WeatherViewData
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class OpenWeatherRepo @Inject constructor(
    private val openWeatherServiceProvider: OpenWeatherServiceProvider,
    private val sharedPrefDataSource: SharedPrefDataSource,
    private val locationService: LocationService
) {
    //TODO: Units will move to a setting page
    val currentTemperatureUnit = "°C"

    suspend fun getWeatherForCoordinates(
        latitude: Double,
        longitude: Double
    ): Response<WeatherViewData> {

        //TODO: Sanity check location info

        val response = openWeatherServiceProvider.getWeatherByCoordinates(
            latitude = latitude,
            longitude = longitude
        )

        return handleWeatherResponse(response)
    }

    suspend fun getWeatherByCity(
        name: String
    ): Response<WeatherViewData> {
        val weatherData = openWeatherServiceProvider.getWeatherByCity(name = name)

        return handleWeatherResponse(weatherData)
    }

    suspend fun getWeatherByCurrentLocation(): Response<WeatherViewData> = coroutineScope {
        val currentLocation = async { locationService.getCurrentLocation() }
        val locData = currentLocation.await()
        val lat = locData.latitude
        val lon = locData.longitude
        return@coroutineScope if (lat == 0.0 && lon == 0.0) {
            Response.error("Could not fetch current location", ErrorType.LOCATION_ACCESS)
        } else {
            getWeatherForCoordinates(lat, lon)
        }
    }

    private suspend fun handleWeatherResponse(response: retrofit2.Response<WeatherDetail>): Response<WeatherViewData> {
        if (response.isSuccessful) {
            //TODO: Cache data
            val weatherData = transformToWeatherViewData(response.body())
            return if (weatherData != null) {
                sharedPrefDataSource.saveLocationData(
                    LocationData(
                        response.body()?.coordinates?.lat ?: 0.0,
                        response.body()?.coordinates?.lon ?: 0.0
                    )
                )
                Response.success(weatherData)
            } else {
                Response.error("Parse error", ErrorType.PARSE_ERROR)
            }
        } else {
            return Response.error(response.message(), ErrorType.NETWORK)
        }
    }

    private fun transformToWeatherViewData(wDetail: WeatherDetail?): WeatherViewData? {
        return wDetail?.let {
            WeatherViewData(
                cityName = it.cityName,
                temperature = "${it.temperatureData.temperature} ${currentTemperatureUnit}",
                feelsLike = "${it.temperatureData.feelsLike} ${currentTemperatureUnit}",
                minTemp = "${it.temperatureData.minTemp} $currentTemperatureUnit",
                maxTemp = "${it.temperatureData.maxTemp} $currentTemperatureUnit",
                pressure = "${it.temperatureData.pressure} hPa",
                humidity = "${it.temperatureData.humidity}%",
                windSpeed = "${it.windinfo.speed} m/s",
                windDirection = "${it.windinfo.angle}°",
                sunrise = DateUtil.getTimeFromEpoch(it.sys.sunrise) ?: "---",
                sunset = DateUtil.getTimeFromEpoch(it.sys.sunset) ?: "---",
                title = it.weatherInfo.first().title,
                description = it.weatherInfo.first().description,
                icon = "https://openweathermap.org/img/wn/${it.weatherInfo.first().iconCode}@4x.png"
            )
        }
    }
}