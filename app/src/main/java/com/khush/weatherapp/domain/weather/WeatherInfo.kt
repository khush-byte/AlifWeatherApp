package com.khush.weatherapp.domain.weather

// Data class representing aggregated weather information
data class WeatherInfo(
    // Map of weather data grouped by day, where the key is the day index and the value is a list of WeatherData
    val weatherDataPerDay: Map<Int, List<WeatherData>>,

    // The current weather data for the present or upcoming hour
    val currentWeatherData: WeatherData?
)
