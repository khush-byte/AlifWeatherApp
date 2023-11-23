package com.khush.weatherapp.domain.weather

import java.time.LocalDateTime

// Data class representing weather-related information for a specific time
data class WeatherData(
    // The time for which the weather information is applicable
    val time: LocalDateTime,

    // The temperature in Celsius at the specified time
    val temperatureCelsius: Double,

    // The type of weather at the specified time, categorized by WeatherType
    val weatherType: WeatherType
)
