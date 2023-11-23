package com.khush.weatherapp.presentation

import com.khush.weatherapp.domain.weather.WeatherInfo

// Data class representing the state of the weather information
data class WeatherState(
    // Weather information, defaulting to null
    val weatherInfo: WeatherInfo? = null,

    // Flag indicating whether the data is currently being loaded, defaulting to false
    val isLoading: Boolean = false,

    // Optional error message, defaulting to null
    val error: String? = null
)