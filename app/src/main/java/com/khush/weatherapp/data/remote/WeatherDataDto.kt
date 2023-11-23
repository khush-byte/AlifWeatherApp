package com.khush.weatherapp.data.remote

import com.squareup.moshi.Json

// Data class representing the DTO (Data Transfer Object) for weather-related data
data class WeatherDataDto(
    // List of time strings indicating the time of each data point
    val time: List<String>,

    // List of temperatures at 2 meters above ground for each corresponding time
    @field:Json(name = "temperature_2m")
    val temperatures: List<Double>,

    // List of weather codes for icons
    @field:Json(name = "weathercode")
    val weatherCodes: List<Int>
)
