package com.akshayholla.openweather.data.network;

import com.akshayholla.openweather.common.ConstantsKt;
import com.akshayholla.openweather.data.network.model.WeatherDetail;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherApi {
    @GET("/" + ConstantsKt.api25 + "/weather")
    Single<WeatherDetail> getWeatherByCoordinates(
            @Query("lat") double latitude,
            @Query("lon") double longitude,
            @Query("units") String units,
            @Query("appid") String appid
    );

    @GET("/" + ConstantsKt.api25 + "/weather")
    Single<WeatherDetail> getWeatherByCityName(
            @Query("q") String name,
            @Query("units") String units,
            @Query("appid") String appid
    );
}
