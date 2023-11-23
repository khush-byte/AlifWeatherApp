package com.khush.weatherapp.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Composable function to display the weather forecast for a specific day
@Composable
fun WeatherForecast(
    index: Int,
    state: WeatherState,
    modifier: Modifier = Modifier
) {
    // Retrieve the weather data for the specified day index from the state
    state.weatherInfo?.weatherDataPerDay?.get(index)?.let { data ->
        // Create a vertical column layout to display the hourly weather forecast
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 0.dp)
        ) {
            // Header text indicating the hourly weather forecast
            Text(
                text = "Hourly weather forecast:",
                fontSize = 18.sp,
                color = Color.Gray,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            )

            // Spacer for vertical separation
            Spacer(modifier = Modifier.height(16.dp))

            // LazyRow to horizontally display the hourly weather data
            LazyRow(content = {
                // Iterate through each weatherData item and display it using HourlyWeatherDisplay
                items(data) { weatherData ->
                    HourlyWeatherDisplay(
                        weatherData = weatherData,
                        modifier = Modifier
                            .height(102.dp)
                            .padding(horizontal = 16.dp)
                    )
                }
            })
        }
    }
}