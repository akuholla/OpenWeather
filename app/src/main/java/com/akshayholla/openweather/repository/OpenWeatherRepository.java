package com.akshayholla.openweather.repository;

import com.akshayholla.openweather.common.DateUtil;
import com.akshayholla.openweather.data.SharedPrefDataSource;
import com.akshayholla.openweather.data.network.OpenWeatherServiceProvider;
import com.akshayholla.openweather.data.network.model.WeatherDetail;
import com.akshayholla.openweather.location.LocationService;
import com.akshayholla.openweather.location.model.LocationData;
import com.akshayholla.openweather.weatherDetails.model.WeatherViewData;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class OpenWeatherRepository {
    private OpenWeatherServiceProvider openWeatherServiceProvider;
    private SharedPrefDataSource sharedPrefDataSource;
    private LocationService locationService;

    //TODO: Units will move to a setting page
    private String currentTemperatureUnit = "°C";

    @Inject
    OpenWeatherRepository(OpenWeatherServiceProvider openWeatherServiceProvider, SharedPrefDataSource sharedPrefDataSource, LocationService locationService) {
        this.openWeatherServiceProvider = openWeatherServiceProvider;
        this.sharedPrefDataSource = sharedPrefDataSource;
        this.locationService = locationService;
    }

    public Single<WeatherViewData> getWeatherByCoordinates(double latitude, double longitude) {
        return openWeatherServiceProvider.getWeatherByCoordinates(latitude, longitude).map(item -> transformWeatherDetailToViewData(item));
    }

    public Single<WeatherViewData> getWeatherByCurrentLocation() {
        return locationService.getCurrentLocation().flatMap(location -> getWeatherByCoordinates(location.getLatitude(), location.getLongitude()));
    }

    public Single<WeatherViewData> getWeatherByCityName(String cityName) {
        return openWeatherServiceProvider.getWeatherByCity(cityName).map(item -> transformWeatherDetailToViewData(item));
    }

    private WeatherViewData transformWeatherDetailToViewData(WeatherDetail weatherDetail) {
        //Todo: clean this up
        sharedPrefDataSource.saveLocation(new LocationData(weatherDetail.getCoordinates().getLat(), weatherDetail.getCoordinates().getLon()))
                .subscribe();
        //TODO: Cleanup units!
        return new WeatherViewData(
                weatherDetail.getCityName(),
                "" + weatherDetail.getTemperatureData().getTemperature() + currentTemperatureUnit,
                "" + weatherDetail.getTemperatureData().getFeelsLike() + currentTemperatureUnit,
                "" + weatherDetail.getTemperatureData().getMinTemp() + currentTemperatureUnit,
                "" + weatherDetail.getTemperatureData().getMaxTemp() + currentTemperatureUnit,
                weatherDetail.getWeatherInfo().get(0).getTitle(),
                weatherDetail.getWeatherInfo().get(0).getDescription(),
                "https://openweathermap.org/img/wn/" + weatherDetail.getWeatherInfo().get(0).getIconCode() + "@4x.png",
                weatherDetail.getTemperatureData().getPressure() + "hPa",
                weatherDetail.getTemperatureData().getHumidity() + "%",
                weatherDetail.getWindinfo().getSpeed() + "m/s",
                weatherDetail.getWindinfo().getAngle() + "°",
                DateUtil.INSTANCE.getTimeFromEpoch(weatherDetail.getSys().getSunrise()),
                DateUtil.INSTANCE.getTimeFromEpoch(weatherDetail.getSys().getSunset()));
    }
}
