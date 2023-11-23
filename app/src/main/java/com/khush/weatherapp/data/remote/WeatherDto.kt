package com.khush.weatherapp.data.remote

import com.squareup.moshi.Json

//Data transfer object class for weather information
data class WeatherDto(
    // Representing hourly weather data
    @field:Json(name = "hourly")
    val weatherData: WeatherDataDto
)