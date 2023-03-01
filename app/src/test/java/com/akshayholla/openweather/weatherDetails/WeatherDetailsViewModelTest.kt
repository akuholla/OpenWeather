package com.akshayholla.openweather.weatherDetails

import com.akshayholla.openweather.ImmediateSchedulersRule
import com.akshayholla.openweather.MainDispatcherRule
import com.akshayholla.openweather.location.model.LocationData
import com.akshayholla.openweather.repository.OpenWeatherRepository
import com.akshayholla.openweather.repository.UserRepository
import com.akshayholla.openweather.weatherDetails.model.WeatherViewData
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.plugins.RxJavaPlugins
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

    @get:Rule
    val immediateSchedulersRule = ImmediateSchedulersRule()

    lateinit var subject: WeatherDetailsViewModel

    val flow = MutableSharedFlow<LocationData>()

    val mockWeatherRepo: OpenWeatherRepository = mock()
    val mockUserRepo: UserRepository = mock()


    @Before
    fun setup() {
        //TODO: Since we have call a method on init, base setup for mocked responses cannot be setup since they are different for different tests
        RxJavaPlugins.setErrorHandler { throwable: Throwable ->
            println(
                throwable.message
            )
        }
    }

    @Test
    fun `Show loading state when view model is first initialized`() = runTest {
        whenever(mockUserRepo.getSavedLoc()).thenReturn(Flowable.just(LocationData(360.0, 360.0)))

        subject = WeatherDetailsViewModel(mockWeatherRepo, mockUserRepo, testDispatcher)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            subject.uiState.collect()
        }

        assertEquals(subject.uiState.value, WeatherDetailsState.Loading)
    }

    @Test
    fun `User location is fetched on init`() = runTest {
        whenever(mockUserRepo.getSavedLoc()).thenReturn(Flowable.just(LocationData(360.0, 360.0)))

        subject = WeatherDetailsViewModel(mockWeatherRepo, mockUserRepo, testDispatcher)
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            subject.uiState.collect()
        }

        verify(mockUserRepo).getSavedLoc()
    }

    @Test
    fun `If User location is 360,360 then get location current location`() = runTest {
        whenever(mockUserRepo.getSavedLoc()).thenReturn(Flowable.just(LocationData(360.0, 360.0)))

        subject = WeatherDetailsViewModel(mockWeatherRepo, mockUserRepo, testDispatcher)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            subject.uiState.collect()
        }

        verify(mockWeatherRepo).getWeatherByCurrentLocation()
    }

    @Test
    fun `If User location is other than 0 then get location by coordinates`() = runTest {
        whenever(mockUserRepo.getSavedLoc()).thenReturn(Flowable.just(LocationData(360.0, 360.0)))
        whenever(mockUserRepo.getSavedLoc()).thenReturn(Flowable.just(LocationData(36.10, 40.50)))

        subject = WeatherDetailsViewModel(mockWeatherRepo, mockUserRepo, testDispatcher)


        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            subject.uiState.collect()
        }

        verify(mockWeatherRepo).getWeatherByCoordinates(36.10, 40.50)
    }

    @Test
    fun `If User types a city name get weather by city name and it is empty`() = runTest {
        whenever(mockUserRepo.getSavedLoc()).thenReturn(Flowable.just(LocationData(360.0, 360.0)))

        subject = WeatherDetailsViewModel(mockWeatherRepo, mockUserRepo, testDispatcher)

        val cityName = ""

        subject.getWeatherDataByCityName(cityName)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            subject.uiState.collect()
        }

        assertEquals(subject.uiState.value, WeatherDetailsState.CityNotFound)
    }

    @Test
    fun `If User types a city name get weather by city name and it is a valid name`() = runTest {
        whenever(mockUserRepo.getSavedLoc()).thenReturn(Flowable.just(LocationData(360.0, 360.0)))
        whenever(mockWeatherRepo.getWeatherByCityName(Mockito.anyString())).thenReturn(
            Single.just(
                WeatherViewData("", "", "", "", "", "", "", "", "", "", "", "", "", "")
            )
        )

        subject = WeatherDetailsViewModel(mockWeatherRepo, mockUserRepo, testDispatcher)

        val cityName = "London"

        subject.getWeatherDataByCityName(cityName)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            subject.uiState.collect()
        }

        verify(mockWeatherRepo).getWeatherByCityName(cityName)
    }

    //TODO: Write more tests for handleWeatherResponseData...
}