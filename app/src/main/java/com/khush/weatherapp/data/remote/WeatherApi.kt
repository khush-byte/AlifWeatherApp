package com.khush.weatherapp.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

// WeatherApi interface for defining API requests related to weather information
interface WeatherApi {
    // Create a GET request to retrieve weather data for a given latitude and longitude
    // The response is handled asynchronously using suspend functions
    @GET("v1/forecast?hourly=temperature_2m,weathercode")
    suspend fun getWeatherData(
        // Latitude parameter for specifying the location
        @Query("latitude") lat: Double,

        // Longitude parameter for specifying the location
        @Query("longitude") long: Double
    ): WeatherDto
}