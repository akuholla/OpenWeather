package com.akshayholla.openweather.data

import android.content.Context
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import com.akshayholla.openweather.common.dataStore
import com.akshayholla.openweather.location.model.LocationData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPrefDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    val LAT_DATA = doublePreferencesKey("lat_data")
    val LON_DATA = doublePreferencesKey("lon_data")

    val getSavedLocationData: Flow<LocationData> = context.dataStore.data
        .map { preferences ->
            val lat = preferences[LAT_DATA] ?: 0.0
            val lon = preferences[LON_DATA] ?: 0.0
            return@map LocationData(lat, lon)
        }

    suspend fun saveLocationData(locationData: LocationData) {
        context.dataStore.edit { locData ->
            locData[LAT_DATA] = locationData.latitude
            locData[LON_DATA] = locationData.longitude
        }
    }
}