package com.akshayholla.openweather.repository

import com.akshayholla.openweather.data.SharedPrefDataSource
import com.akshayholla.openweather.location.model.LocationData
import javax.inject.Inject

class UserRepo @Inject constructor(
    val sharedPrefDataSource: SharedPrefDataSource
) {
    fun getLastKnownLocation() = sharedPrefDataSource.getSavedLocationData()

    suspend fun saveLocationData(locationData: LocationData) =
        sharedPrefDataSource.saveLocationData(locationData = locationData)
}