package com.khush.weatherapp.domain.repository

import com.khush.weatherapp.domain.util.Resource
import com.khush.weatherapp.domain.weather.WeatherInfo

// Interface defining the contract for retrieving weather data
interface WeatherRepository {

    // A suspend function that fetches weather data based on the provided latitude, longitude,
    // and index(day number from 0)
    // Returns a Resource<WeatherInfo> indicating success or failure, wrapping the retrieved
    // data or an error
    suspend fun getWeatherData(lat: Double, long: Double, index: Int): Resource<WeatherInfo>
}