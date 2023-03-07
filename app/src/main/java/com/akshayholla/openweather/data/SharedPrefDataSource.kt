package com.akshayholla.openweather.data

import android.content.Context
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.akshayholla.openweather.common.dataStore
import com.akshayholla.openweather.location.model.LocationData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPrefDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    val LAT_DATA = doublePreferencesKey("lat_data")
    val LON_DATA = doublePreferencesKey("lon_data")
    val UNIT_DATA = stringPreferencesKey("unit_data")

    fun getSavedLocationData(): Flow<LocationData> =
        context.dataStore.data
            .map { preferences ->
                val lat = preferences[LAT_DATA] ?: 360.0
                val lon = preferences[LON_DATA] ?: 360.0
                return@map LocationData(lat, lon)
            }

    fun getUserUnit(): Flow<String> =
        context.dataStore.data
            .map { preferences ->
                return@map preferences[UNIT_DATA] ?: "metric"
            }

    suspend fun saveLocationData(locationData: LocationData) {
        context.dataStore.edit { locData ->
            locData[LAT_DATA] = locationData.latitude
            locData[LON_DATA] = locationData.longitude
        }
    }

    suspend fun saveUserUnit(unit: String) {
        context.dataStore.edit { data ->
            data[UNIT_DATA] = unit
        }
    }
}