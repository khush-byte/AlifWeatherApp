package com.khush.weatherapp.data.repository

import com.khush.weatherapp.data.mappers.toWeatherInfo
import com.khush.weatherapp.data.remote.WeatherApi
import com.khush.weatherapp.domain.repository.WeatherRepository
import com.khush.weatherapp.domain.util.Resource
import com.khush.weatherapp.domain.weather.WeatherInfo
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi
): WeatherRepository {

    override suspend fun getWeatherData(lat: Double, long: Double, index: Int): Resource<WeatherInfo> {
        return try {
            Resource.Success(
                data = api.getWeatherData(
                    lat = lat,
                    long = long
                ).toWeatherInfo(index)
            )
        } catch(e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "An unknown error occurred.")
        }
    }
}