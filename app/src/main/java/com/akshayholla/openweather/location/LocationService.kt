package com.akshayholla.openweather.location

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.akshayholla.openweather.location.model.LocationData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.suspendCoroutine

@Singleton
class LocationService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)


    //Alternate way of converting callbacks to coroutines
    @SuppressLint("MissingPermission")
    suspend fun getCurrentLoc() : LocationData = suspendCoroutine {
        cont ->
        if (isLocationPermissionGranted()) {
            fusedLocationClient.lastLocation.addOnSuccessListener {
                cont.resumeWith(Result.success(LocationData(it.latitude, it.longitude)))
            }.addOnFailureListener {
                cont.resumeWith(Result.success(LocationData(360.0, 360.0)))
            }
        } else {
            cont.resumeWith(Result.success(LocationData(360.0, 360.0)))
        }
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(): Flow<LocationData> = callbackFlow {
        val latitude = 360.0
        val longitude = 360.0
        if (isLocationPermissionGranted()) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                trySend(
                    LocationData(
                        latitude = location.latitude, longitude = location.longitude
                    )
                )
            }.addOnFailureListener {
                trySend(
                    LocationData(
                        latitude = latitude, longitude = longitude
                    )
                )
            }
        } else {
            trySend(
                LocationData(
                    latitude = latitude, longitude = longitude
                )
            )
        }

        awaitClose {
            //Close the api callback
            fusedLocationClient.flushLocations()
        }
    }

    private fun isLocationPermissionGranted(): Boolean = ContextCompat.checkSelfPermission(
        context, android.Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}