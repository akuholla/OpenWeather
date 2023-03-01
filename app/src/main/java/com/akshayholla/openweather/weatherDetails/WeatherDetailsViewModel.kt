package com.akshayholla.openweather.weatherDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akshayholla.openweather.common.ErrorType
import com.akshayholla.openweather.common.Response
import com.akshayholla.openweather.common.Status
import com.akshayholla.openweather.di.MainDispatcher
import com.akshayholla.openweather.location.model.LocationData
import com.akshayholla.openweather.repository.OpenWeatherRepository
import com.akshayholla.openweather.repository.UserRepository
import com.akshayholla.openweather.weatherDetails.model.WeatherViewData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherDetailsViewModel @Inject constructor(
    private val openWeatherRepo: OpenWeatherRepository,
    private val userRepo: UserRepository,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _uiState = MutableStateFlow<WeatherDetailsState>(WeatherDetailsState.Loading)
    val uiState: StateFlow<WeatherDetailsState> = _uiState

    init {
        getSavedLocationWeather()
    }

    fun getSavedLocationWeather() {
        showLoading()

        val lastKnownLocation = userRepo.getSavedLoc().first(LocationData(360.0, 360.0))

        //TODO: dispose these correctly: autodispose?
        lastKnownLocation
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                //Try to get from current location
                getWeatherFromCurrentLocation()
            }
            .onErrorComplete()
            .doOnSuccess {
                val lat = it.latitude
                val lon = it.longitude
                // ask user to either search for new location or enable permission to get current location weather data
                if (lat != 360.0 && lon != 360.0) {
                    // get weather from last known coordinates
                    getWeatherFromCoordinates(lat, lon)
                } else {
                    //Try to get from current location
                    getWeatherFromCurrentLocation()
                }
            }
            .subscribe()
    }

    fun getWeatherDataByCityName(cityName: String) {
        showLoading()

        //TODO : Handle this error better
        if (cityName.isEmpty()) {
            _uiState.value = WeatherDetailsState.CityNotFound
            return
        }

        //TODO : Error for different response codes are not yet handled
        openWeatherRepo.getWeatherByCityName(cityName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                handleWeatherResponseData(Response.error(it.message ?: "", ErrorType.NETWORK))
                it.printStackTrace()
            }
            .onErrorComplete()
            .map {
                try {
                    handleWeatherResponseData(Response.success(it))
                } catch (e: java.lang.Exception) {
                    handleWeatherResponseData(Response.error(e.message ?: "", ErrorType.NETWORK))
                    e.printStackTrace()
                }
            }.subscribe()
    }

    private fun getWeatherFromCurrentLocation() {
        //TODO: Properly handle onError
        openWeatherRepo.getWeatherByCurrentLocation()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).doOnError {
                handleWeatherResponseData(Response.error(it.message ?: "", ErrorType.NETWORK))
                it.printStackTrace()
            }.onErrorComplete().map {
                handleWeatherResponseData(Response.success(it))
            }.subscribe()
    }

    private fun getWeatherFromCoordinates(lat: Double, lon: Double) {
        //TODO: Dispose these
        openWeatherRepo.getWeatherByCoordinates(lat, lon)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).doOnError {
                handleWeatherResponseData(Response.error(it.message ?: "", ErrorType.NETWORK))
                it.printStackTrace()
            }.onErrorComplete().map {
                handleWeatherResponseData(Response.success(it))
            }.subscribe()
    }

    private fun showLoading() {
        _uiState.value = WeatherDetailsState.Loading
    }

    private fun handleWeatherResponseData(response: Response<WeatherViewData>) {
        viewModelScope.launch(mainDispatcher) {
            when (response.status) {
                Status.SUCCESS -> {
                    if (response.data != null) {
                        _uiState.value = WeatherDetailsState.WeatherData(response.data)
                    } else {
                        _uiState.value = WeatherDetailsState.NetworkError
                    }
                }
                Status.ERROR -> {
                    when (response.errorType) {
                        ErrorType.NETWORK -> {
                            _uiState.value = WeatherDetailsState.NetworkError
                        }
                        ErrorType.LOCATION_ACCESS -> {
                            _uiState.value = WeatherDetailsState.LocationPermission
                        }
                        ErrorType.PARSE_ERROR -> {
                            _uiState.value = WeatherDetailsState.NetworkError
                        }
                        null -> {
                            _uiState.value = WeatherDetailsState.NetworkError
                        }
                    }
                }
            }
        }
    }
}