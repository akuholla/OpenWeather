package com.akshayholla.openweather.repository;

import androidx.datastore.preferences.core.Preferences;

import com.akshayholla.openweather.data.SharedPrefDataSource;
import com.akshayholla.openweather.location.model.LocationData;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class UserRepository {
    private SharedPrefDataSource sharedPrefDataSource;
    @Inject
    public UserRepository(SharedPrefDataSource sharedPrefDataSource) {
        this.sharedPrefDataSource = sharedPrefDataSource;
    }

    public Flowable<LocationData> getSavedLoc() {
        return sharedPrefDataSource.getSavedLocationData();
    }

    public Single<Preferences> saveLoc(LocationData locationData) {
        return sharedPrefDataSource.saveLocation(locationData);
    }


}
