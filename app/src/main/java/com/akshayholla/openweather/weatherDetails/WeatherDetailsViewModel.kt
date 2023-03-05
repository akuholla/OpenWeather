package com.akshayholla.openweather.weatherDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akshayholla.openweather.common.ErrorType
import com.akshayholla.openweather.common.Response
import com.akshayholla.openweather.common.Status
import com.akshayholla.openweather.di.MainDispatcher
import com.akshayholla.openweather.repository.OpenWeatherRepo
import com.akshayholla.openweather.repository.UserRepo
import com.akshayholla.openweather.weatherDetails.model.WeatherViewData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherDetailsViewModel @Inject constructor(
    private val openWeatherRepo: OpenWeatherRepo,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _uiState = MutableStateFlow<WeatherDetailsState>(WeatherDetailsState.Loading)
    val uiState: StateFlow<WeatherDetailsState> = _uiState

    init {
        getWeatherData()
    }
    
    fun locationPermissionGranted() {
        viewModelScope.launch {
            handleWeatherResponseData(openWeatherRepo.getWeatherFromCurrentLocation())
        }
    }

    private fun getWeatherData() {
        viewModelScope.launch {
            handleWeatherResponseData(openWeatherRepo.getDefaultWeatherData())
        }
    }

    fun getWeatherDataByCityName(cityName: String) {
        showLoading()

        //TODO : Handle this error better
        if (cityName.isEmpty()) {
            _uiState.value = WeatherDetailsState.CityNotFound
            return
        }

        viewModelScope.launch {
            val locationResponse = openWeatherRepo.getWeatherByCity(cityName)

            handleWeatherResponseData(locationResponse)
        }
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