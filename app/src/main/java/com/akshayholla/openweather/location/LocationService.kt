package com.akshayholla.openweather.location

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.akshayholla.openweather.location.model.LocationData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    @OptIn(ExperimentalCoroutinesApi::class)
    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): LocationData = suspendCancellableCoroutine { continuation ->
        var latitude = 0.0
        var longitude = 0.0
        if (isLocationPermissionGranted()) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                continuation.resume(
                    LocationData(
                        latitude = location.latitude, longitude = location.longitude
                    ), onCancellation = {
                        //TODO: Handle this better
                        it.printStackTrace()
                    })
            }
        } else {
            continuation.resume(LocationData(
                latitude = latitude, longitude = longitude
            ), onCancellation = {
                //TODO: Handle this better
                it.printStackTrace()
            })
        }
    }

    private fun isLocationPermissionGranted(): Boolean = ContextCompat.checkSelfPermission(
        context, android.Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}