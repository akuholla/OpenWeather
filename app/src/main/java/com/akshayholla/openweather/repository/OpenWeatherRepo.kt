package com.akshayholla.openweather.repository

import com.akshayholla.openweather.common.DateUtil
import com.akshayholla.openweather.common.ErrorType
import com.akshayholla.openweather.common.INVALID_LOCATION_VALUE
import com.akshayholla.openweather.common.Response
import com.akshayholla.openweather.data.Unit
import com.akshayholla.openweather.data.network.OpenWeatherServiceProvider
import com.akshayholla.openweather.data.network.model.WeatherDetail
import com.akshayholla.openweather.location.LocationService
import com.akshayholla.openweather.location.model.LocationData
import com.akshayholla.openweather.weatherDetails.model.WeatherViewData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OpenWeatherRepo @Inject constructor(
    private val openWeatherServiceProvider: OpenWeatherServiceProvider,
    private val locationService: LocationService,
    private val userRepo: UserRepo
) {
    suspend fun getDefaultWeatherData(): Response<WeatherViewData> {
        val lastKnownLocation = userRepo.getLastKnownLocation()

        return if (isLocationValid(lastKnownLocation)) {
            getWeatherForCoordinates(
                latitude = lastKnownLocation.latitude,
                longitude = lastKnownLocation.longitude
            )
        } else {
            //Try to get from current location
            getWeatherFromCurrentLocation()
        }
    }

    suspend fun getWeatherFromCurrentLocation(): Response<WeatherViewData> {
        val currentLocation = locationService.getCurrentLocation().first()

        if (isLocationValid(currentLocation)) {
            return getWeatherForCoordinates(currentLocation.latitude, currentLocation.longitude)
        } else {
            return Response.error("Could not fetch current location.", ErrorType.LOCATION_ACCESS)
        }
    }

    suspend fun getWeatherForCoordinates(
        latitude: Double,
        longitude: Double
    ): Response<WeatherViewData> {

        //TODO: Sanity check location info
        val unit = getTemperatureUnit()
        val response = openWeatherServiceProvider.getWeatherByCoordinates(
            latitude = latitude,
            longitude = longitude,
            unit.param
        )

        return handleWeatherResponse(response)
    }

    suspend fun getWeatherByCity(
        name: String
    ): Response<WeatherViewData> {
        val unit = getTemperatureUnit()
        val weatherData = openWeatherServiceProvider.getWeatherByCity(name = name, unit.param)

        return handleWeatherResponse(weatherData)
    }

    suspend fun getWeatherByCurrentLocation(): Flow<Response<WeatherViewData>> =
        locationService.getCurrentLocation().map { locData ->
            val lat = locData.latitude
            val lon = locData.longitude
            return@map if (lat == 0.0 && lon == 0.0) {
                Response.error("Could not fetch current location", ErrorType.LOCATION_ACCESS)
            } else {
                getWeatherForCoordinates(lat, lon)
            }
        }

    private suspend fun handleWeatherResponse(response: retrofit2.Response<WeatherDetail>): Response<WeatherViewData> {
        if (response.isSuccessful) {
            val units = getTemperatureUnit()
            val weatherData = transformToWeatherViewData(response.body(), units)
            return if (weatherData != null) {
                userRepo.saveLocationData(
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

    private fun transformToWeatherViewData(wDetail: WeatherDetail?, unit: Unit): WeatherViewData? {
        return wDetail?.let {
            WeatherViewData(
                cityName = it.cityName,
                temperature = "${it.temperatureData.temperature} ${unit.symbol}",
                feelsLike = "${it.temperatureData.feelsLike} ${unit.symbol}",
                minTemp = "${it.temperatureData.minTemp} ${unit.symbol}",
                maxTemp = "${it.temperatureData.maxTemp} ${unit.symbol}",
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

    private fun isLocationValid(locData: LocationData): Boolean {
        return locData.latitude != INVALID_LOCATION_VALUE && locData.longitude != INVALID_LOCATION_VALUE
    }

    private suspend fun getTemperatureUnit(): Unit {
        val userUnit = userRepo.getUnit()
        return when (userUnit) {
            "metric" -> {
                Unit.METRIC
            }
            "imperial" -> {
                Unit.IMPERIAL
            }
            else -> {
                Unit.METRIC
            }
        }
    }
}