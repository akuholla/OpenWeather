package com.akshayholla.openweather.weatherDetails

import com.akshayholla.openweather.MainDispatcherRule
import com.akshayholla.openweather.location.model.LocationData
import com.akshayholla.openweather.repository.OpenWeatherRepo
import com.akshayholla.openweather.repository.UserRepo
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherDetailsViewModelTest {
    val testScheduler = TestCoroutineScheduler()
    val testDispatcher = StandardTestDispatcher(testScheduler)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    lateinit var subject: WeatherDetailsViewModel

    val flow = MutableSharedFlow<LocationData>()

    val mockWeatherRepo: OpenWeatherRepo = mock()
    val mockUserRepo: UserRepo = mock()


    @Before
    fun setup() {
        // TODO: if code is moved out of init block in the viewmodel,
        //      we could have the viewmodel initialized here before every test
        whenever(mockUserRepo.getLocationData()).thenReturn(flow)

        subject = WeatherDetailsViewModel(mockWeatherRepo, mockUserRepo, testDispatcher)
    }

    @Test
    fun `Show loading state when view model is first initialized`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            subject.uiState.collect()
        }

        assertEquals(subject.uiState.value, WeatherDetailsState.Loading)
    }

    @Test
    fun `User location is fetched on init`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            subject.uiState.collect()
        }

        verify(mockUserRepo).getLocationData()
    }

    @Test
    fun `If User location is 0,0 then get location current location`() = runTest {
        flow.emit(LocationData(360.0, 360.0))

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            subject.uiState.collect()
        }

        verify(mockWeatherRepo).getWeatherByCurrentLocation()
    }

    @Test
    fun `If User location is other than 0 then get location by coordinates`() = runTest {
        flow.emit(LocationData(36.10, 40.50))

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            subject.uiState.collect()
        }

        verify(mockWeatherRepo).getWeatherForCoordinates(36.10, 40.50)
    }

    @Test
    fun `If User types a city name get weather by city name and it is empty`() = runTest {
        val cityName = ""

        subject.getWeatherDataByCityName(cityName)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            subject.uiState.collect()
        }

        assertEquals(subject.uiState.value, WeatherDetailsState.CityNotFound)
    }

    @Test
    fun `If User types a city name get weather by city name and it is a valid name`() = runTest {
        val cityName = "London"

        subject.getWeatherDataByCityName(cityName)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            subject.uiState.collect()
        }

        verify(mockWeatherRepo).getWeatherByCity(cityName)
    }

    //TODO: Write more tests for handleWeatherResponseData...
}