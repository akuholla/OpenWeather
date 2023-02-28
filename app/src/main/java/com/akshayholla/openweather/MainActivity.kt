package com.akshayholla.openweather

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.akshayholla.openweather.navigation.Routes
import com.akshayholla.openweather.settings.SettingsView
import com.akshayholla.openweather.ui.theme.OpenWeatherTheme
import com.akshayholla.openweather.weatherDetails.WeatherAppScreen
import com.akshayholla.openweather.weatherDetails.WeatherDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val weatherDetailsViewModel: WeatherDetailsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //TODO: Explain user about why we would like their location access before asking them for permission
        if (isLocationPermissionNotGranted()) {
            locationPermissionRequest.launch(
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }

        setContent {
            OpenWeatherTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainApp(weatherDetailsViewModel)
                }
            }
        }
    }

    //TODO: Probably move this to a location permission wrapper
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(android.Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                weatherDetailsViewModel.getSavedLocationWeather()
            }
            permissions.getOrDefault(android.Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                weatherDetailsViewModel.getSavedLocationWeather()
            }
            else -> {
            }
        }
    }

    //TODO: Duplicate here and in LocationService -> consolidate in location permission
    private fun isLocationPermissionNotGranted(): Boolean = ContextCompat.checkSelfPermission(
        this,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    ) != PackageManager.PERMISSION_GRANTED
}

@Composable
fun MainApp(wDetailsViewModel: WeatherDetailsViewModel) {
    val navController = rememberNavController()
    var selectedPage by remember { mutableStateOf(Routes.MAIN.name) }

    Scaffold(bottomBar = {
        BottomNavigation {
            BottomNavigationItem(selected = selectedPage == Routes.MAIN.name, onClick = {
                selectedPage = Routes.MAIN.name
                navController.navigate(Routes.MAIN.name)
            }, icon = {
                Icon(imageVector = Icons.Default.Home, contentDescription = "Home")
            })
            BottomNavigationItem(selected = selectedPage == Routes.SETTINGS.name, onClick = {
                selectedPage = Routes.SETTINGS.name
                navController.navigate(Routes.SETTINGS.name)
            }, icon = {
                Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings")
            })
        }

    }) { paddingValues ->
        //TODO: Move routes out of here and separate the routes
        NavHost(navController = navController, startDestination = Routes.MAIN.name) {
            composable(Routes.MAIN.name) {
                WeatherAppScreen(
                    modifier = Modifier.padding(paddingValues),
                    wDetailsViewModel
                )
            }

            composable(Routes.SETTINGS.name) {
                SettingsView()
            }
        }
    }
}

