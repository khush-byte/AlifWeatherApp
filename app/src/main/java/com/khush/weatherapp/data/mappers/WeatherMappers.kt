package com.khush.weatherapp.data.mappers

import com.khush.weatherapp.data.remote.WeatherDataDto
import com.khush.weatherapp.data.remote.WeatherDto
import com.khush.weatherapp.domain.weather.WeatherData
import com.khush.weatherapp.domain.weather.WeatherInfo
import com.khush.weatherapp.domain.weather.WeatherType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// Create a data class to represent indexed weather data, pairing each data point with its index.
private data class IndexedWeatherData(
    val index: Int,
    val data: WeatherData
)

// Function for converting WeatherDataDto to a Map<Int, List<WeatherData>> format.
fun WeatherDataDto.toWeatherDataMap(): Map<Int, List<WeatherData>> {
    return time.mapIndexed { index, time ->
        // Extract relevant data for each time index and create IndexedWeatherData objects.
        val temperature = temperatures[index]
        val weatherCode = weatherCodes[index]
        IndexedWeatherData(
            index = index,
            data = WeatherData(
                time = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME),
                temperatureCelsius = temperature,
                weatherType = WeatherType.fromWMO(weatherCode)
            )
        )
    }.groupBy {
        // Group IndexedWeatherData objects by their index divided by 24 to represent days
        it.index / 24
    }.mapValues {
        // Extract the WeatherData from each group and create a Map<Int, List<WeatherData>>
        it.value.map { it.data }
    }
}

// Function for converting WeatherDto to WeatherInfo, focusing on a specific index.
fun WeatherDto.toWeatherInfo(index: Int): WeatherInfo {
    // Convert weatherData to a Map<Int, List<WeatherData>> format.
    val weatherDataMap = weatherData.toWeatherDataMap()

    // Get the current date and time
    val now = LocalDateTime.now()

    // Find the relevant WeatherData for the specified index and current time
    val currentWeatherData = weatherDataMap[index]?.find {
        // Adjust the hour to find the WeatherData for the current or upcoming hour
        val hour = if(now.minute < 30) now.hour else now.hour + 1
        it.time.hour == hour
    }

    // Create a WeatherInfo object with the converted data
    return WeatherInfo(
        weatherDataPerDay = weatherDataMap,
        currentWeatherData = currentWeatherData
    )
}