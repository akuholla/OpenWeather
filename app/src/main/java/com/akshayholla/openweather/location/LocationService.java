package com.akshayholla.openweather.location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

import com.akshayholla.openweather.location.model.LocationData;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;
import io.reactivex.rxjava3.core.Single;

@Singleton
public class LocationService {
    private Context context;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Inject
    public LocationService(
            @ApplicationContext Context context
    ) {
        this.context = context;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
    }

    @SuppressLint("MissingPermission")
    public Single<LocationData> getCurrentLocation() {
        return Single.create(singleSub -> {
            if (isLocationPermissionGranted()) {
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                    singleSub.onSuccess(new LocationData(location.getLatitude(), location.getLongitude()));
                });
            } else {
                singleSub.onSuccess(new LocationData(360.0, 360.0));
            }
        });
    }

    private boolean isLocationPermissionGranted() {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
}
