package com.akshayholla.openweather.weatherDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.akshayholla.openweather.ui.theme.OpenWeatherTheme
import com.akshayholla.openweather.ui.theme.Typography
import com.akshayholla.openweather.weatherDetails.model.WeatherViewData

//TODO: Layout for landscape orientation
//Stateful weather app screen
@Composable
fun WeatherAppScreen(
    viewModel: WeatherDetailsViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            var searchText by rememberSaveable { mutableStateOf("") }
            OutlinedTextField(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                value = searchText, onValueChange = {
                    searchText = it
                    //TODO: Show suggestions for US cities
                }, placeholder = {
                    Text(text = "Enter a U.S city name")
                }, leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear",
                        modifier = Modifier.clickable {
                            searchText = ""
                        })
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions {
                    viewModel.getWeatherDataByCityName(searchText)
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (uiState) {
                WeatherDetailsState.CityNotFound -> {
                    SearchResultNotFound()
                }
                WeatherDetailsState.Loading -> {
                    LoadingContent()
                }
                WeatherDetailsState.LocationPermission -> {
                    LocationIssue()
                }
                WeatherDetailsState.NetworkError -> {
                    GenericError()
                }
                is WeatherDetailsState.WeatherData -> {
                    WeatherDetails(weatherViewData = (uiState as WeatherDetailsState.WeatherData).weatherData)
                }
            }
        }
    }

    fun onLocationPermissionAction() {
        viewModel.getSavedLocationWeather()
    }
}

//Stateless weather app content screen
@Composable
fun WeatherDetails(weatherViewData: WeatherViewData) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        Card(
            modifier = Modifier.padding(8.dp),
            elevation = 3.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Text(
                    text = weatherViewData.cityName, style = Typography.h2
                )
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                )
                Row(
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Box(
                        modifier = Modifier
                            .height(200.dp)
                            .fillMaxWidth(0.5f)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(weatherViewData.icon)
                                .crossfade(true).build(),
                            contentDescription = weatherViewData.title,
                            contentScale = ContentScale.FillHeight,
                            modifier = Modifier.fillMaxHeight()
                        )
                    }
                    Column(
                        modifier = Modifier.padding(4.dp),
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(
                            text = weatherViewData.temperature,
                            style = Typography.h4,
                            modifier = Modifier.wrapContentHeight()
                        )
                        TemperatureView(
                            title = "feels",
                            data = weatherViewData.feelsLike,
                        )
                        TemperatureView(
                            title = "min", data = weatherViewData.minTemp
                        )
                        TemperatureView(
                            title = "max",
                            data = weatherViewData.maxTemp,
                        )
                    }
                }

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                )
                Text(
                    text = weatherViewData.title,
                    style = Typography.h4,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp)
                )
                Text(
                    text = weatherViewData.description,
                    style = Typography.h5,
                    modifier = Modifier.padding(start = 8.dp)
                )
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                )
                Row(
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "Sun",
                        style = Typography.h4,
                        modifier = Modifier.weight(2f)
                    )
                    TemperatureView(
                        title = "rise",
                        data = weatherViewData.sunrise,
                        modifier = Modifier.weight(1f)
                    )
                    TemperatureView(
                        title = "set",
                        data = weatherViewData.sunset,
                        modifier = Modifier.weight(1f)
                    )
                }
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                )
                Row(
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "Wind",
                        style = Typography.h4,
                        modifier = Modifier.weight(2f)
                    )
                    TemperatureView(
                        title = "speed",
                        data = weatherViewData.windSpeed,
                        modifier = Modifier.weight(1f)
                    )
                    TemperatureView(
                        title = "angle",
                        data = weatherViewData.windDirection,
                        modifier = Modifier.weight(1f)
                    )
                }
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                )
                Row(
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "Feels",
                        style = Typography.h4,
                        modifier = Modifier.weight(2f)
                    )
                    TemperatureView(
                        title = "pressure",
                        data = weatherViewData.pressure,
                        modifier = Modifier.weight(1f)
                    )
                    TemperatureView(
                        title = "humidity",
                        data = weatherViewData.humidity,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun TemperatureView(title: String, data: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(4.dp)) {
        Text(
            text = title,
            style = Typography.subtitle2,
        )
        Text(
            text = data,
            style = Typography.subtitle1,
        )
    }
}

@Composable
fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun SearchResultNotFound() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp), contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Could not find weather for current city. Please try again.",
            style = Typography.h2
        )
    }
}

@Composable
fun LocationIssue() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp), contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Could not find weather for current location. Please check your location permission and try again.",
            style = Typography.h2
        )
    }
}

@Composable
fun GenericError() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp), contentAlignment = Alignment.Center
    ) {
        Text(text = "Something went wrong. Please try again.", style = Typography.h2)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewOfGenericError() {
    OpenWeatherTheme {
        GenericError()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewOfLoading() {
    OpenWeatherTheme {
        LoadingContent()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewOfSearchResultNotFound() {
    OpenWeatherTheme {
        SearchResultNotFound()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewOfWeatherDetails() {
    OpenWeatherTheme {
        WeatherDetails(
            WeatherViewData(
                cityName = "San Jose",
                temperature = "34.0 C",
                feelsLike = "30.0 C",
                minTemp = "26.0 C",
                maxTemp = "40.0 C",
                title = "Snow",
                description = "Winter is coming!",
                icon = "http://...../",
                pressure = "999.0 ",
                humidity = "80%",
                windDirection = "120d",
                windSpeed = "200m/s",
                sunset = "5:30 pm",
                sunrise = "6:00 am"
            )
        )
    }
}