package com.akshayholla.openweather.data;

import android.content.Context;

import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;

import com.akshayholla.openweather.location.model.LocationData;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Singleton
public class SharedPrefDataSource {
    private Context context;
    private RxDataStore<Preferences> dataStore;

    private Preferences.Key<Double> LAT_DATA = new Preferences.Key<Double>("lat_data");
    private Preferences.Key<Double> LON_DATA = new Preferences.Key<Double>("lon_data");

    @Inject
    public SharedPrefDataSource(@ApplicationContext Context context) {
        this.context = context;
        dataStore = new RxPreferenceDataStoreBuilder(context, "openw_loc_data").build();
    }

    public Flowable<LocationData> getSavedLocationData() {
        return dataStore.data().map(preferences -> new LocationData(preferences.get(LAT_DATA), preferences.get(LON_DATA)));
    }

    public Single<Preferences> saveLocation(LocationData locationData) {
        return dataStore.updateDataAsync(prefsIn -> {
            MutablePreferences mutablePreferences = prefsIn.toMutablePreferences();
            mutablePreferences.set(LAT_DATA, locationData.getLatitude());
            mutablePreferences.set(LON_DATA, locationData.getLongitude());
            return Single.just(mutablePreferences);
        });
    }
}
