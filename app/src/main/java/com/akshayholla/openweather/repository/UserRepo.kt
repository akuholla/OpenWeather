package com.akshayholla.openweather.repository

import com.akshayholla.openweather.data.SharedPrefDataSource
import com.akshayholla.openweather.location.model.LocationData
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UserRepo @Inject constructor(
    val sharedPrefDataSource: SharedPrefDataSource
) {
    suspend fun getLastKnownLocation(): LocationData =
        sharedPrefDataSource.getSavedLocationData().first()

    suspend fun saveLocationData(locationData: LocationData) =
        sharedPrefDataSource.saveLocationData(locationData = locationData)
}