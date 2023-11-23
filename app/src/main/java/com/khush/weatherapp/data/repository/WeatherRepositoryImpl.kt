package com.khush.weatherapp.data.repository

import com.khush.weatherapp.data.mappers.toWeatherInfo
import com.khush.weatherapp.data.remote.WeatherApi
import com.khush.weatherapp.domain.repository.WeatherRepository
import com.khush.weatherapp.domain.util.Resource
import com.khush.weatherapp.domain.weather.WeatherInfo
import javax.inject.Inject

// Repository implementation for retrieving weather data
class WeatherRepositoryImpl @Inject constructor(
    // Injected dependency: WeatherApi for making API requests
    private val api: WeatherApi
): WeatherRepository {

    // Implementation of the function to get weather data from the API
    // This function returns a Resource object wrapping either the retrieved data or an error
    override suspend fun getWeatherData(lat: Double, long: Double, index: Int): Resource<WeatherInfo> {
        return try {
            // Attempt to fetch weather data from the API using the provided latitude and longitude
            // Convert the retrieved WeatherDto to WeatherInfo using the specified index
            Resource.Success(
                data = api.getWeatherData(
                    lat = lat,
                    long = long
                ).toWeatherInfo(index)
            )
        } catch(e: Exception) {
            // If an exception occurs during the API request, handle the error
            e.printStackTrace()

            // Create a Resource.Error with the exception message or a default message if unavailable
            Resource.Error(e.message ?: "An unknown error occurred.")
        }
    }
}