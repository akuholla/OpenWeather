package com.akshayholla.openweather.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.akshayholla.openweather.data.Unit
import com.akshayholla.openweather.ui.theme.OpenWeatherTheme
import com.akshayholla.openweather.ui.theme.Typography

@Composable
fun SettingsView(viewModel: SettingsViewModel = hiltViewModel()) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val settingState by produceState<SettingViewState>(
        initialValue = SettingViewState.Loading,
        key1 = viewModel,
        key2 = lifecycle
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.RESUMED) {
            viewModel.getCurrentSettings()
            viewModel.uiState.collect {
                value = it
            }
        }
    }


    Scaffold { paddingValues ->
        when (settingState) {
            is SettingViewState.Loading -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator()
                }
            }
            is SettingViewState.AvailableSettings -> {
                var unitValue by remember { mutableStateOf((settingState as SettingViewState.AvailableSettings).unit == com.akshayholla.openweather.data.Unit.METRIC) }
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = "Settings",
                        style = Typography.h2,
                        modifier = Modifier.padding(8.dp)
                    )
                    Row(
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(text = "Imperial (F)", style = Typography.body1)
                        Switch(
                            checked = unitValue,
                            onCheckedChange = {
                                if (it) {
                                    viewModel.setUnitToMetric()
                                } else {
                                    viewModel.setUnitToImperial()
                                }
                                unitValue = !unitValue
                            })
                        Text(text = "Metric (C)", style = Typography.body1)
                    }
                    Divider(modifier = Modifier.padding(8.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewOfSettings() {
    OpenWeatherTheme {
        SettingsView()
    }
}