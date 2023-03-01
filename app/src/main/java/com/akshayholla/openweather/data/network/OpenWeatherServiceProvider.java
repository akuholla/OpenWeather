package com.akshayholla.openweather.data.network;

import com.akshayholla.openweather.common.ApiEnabler;
import com.akshayholla.openweather.data.network.model.WeatherDetail;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.core.Single;

@Singleton
public class OpenWeatherServiceProvider {
    private OpenWeatherApi openWeatherApi;
    private ApiEnabler apiEnabler;

    @Inject
    OpenWeatherServiceProvider(OpenWeatherApi openWeatherApi,
                               ApiEnabler apiEnabler) {
        this.openWeatherApi = openWeatherApi;
        this.apiEnabler = apiEnabler;
    }

    public Single<WeatherDetail> getWeatherByCity(String name) {
        return openWeatherApi.getWeatherByCityName(name, "metric", apiEnabler.getWeatherApiKey());
    }

    public Single<WeatherDetail> getWeatherByCoordinates(double latitude,
                                                         double longitude) {
        return openWeatherApi.getWeatherByCoordinates(latitude,
                longitude,
                "metric",
                apiEnabler.getWeatherApiKey());
    }
}
