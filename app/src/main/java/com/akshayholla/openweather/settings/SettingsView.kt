package com.akshayholla.openweather.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.akshayholla.openweather.ui.theme.OpenWeatherTheme
import com.akshayholla.openweather.ui.theme.Typography


@Composable
fun SettingsView() {
    Scaffold { paddingValues ->
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
                Switch(checked = true, onCheckedChange = {
                })
                Text(text = "Metric (C)", style = Typography.body1)
            }
            Divider(modifier = Modifier.padding(8.dp))
            Text(text = "Under construction", style = Typography.subtitle1)
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