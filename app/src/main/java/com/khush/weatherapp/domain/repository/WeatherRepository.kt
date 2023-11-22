package com.khush.weatherapp.domain.repository

import com.khush.weatherapp.domain.util.Resource
import com.khush.weatherapp.domain.weather.WeatherInfo

interface WeatherRepository {
    suspend fun getWeatherData(lat: Double, long: Double, index: Int): Resource<WeatherInfo>
}