package com.khush.weatherapp.di

import android.app.Application
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.khush.weatherapp.data.remote.WeatherApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

// Dagger Hilt module for providing dependencies in the application scope.
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Provides a singleton instance of WeatherApi using Retrofit for weather data
    @Provides
    @Singleton
    fun provideWeatherApi(): WeatherApi {
        return Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }

    // Provides a singleton instance of FusedLocationProviderClient using LocationServices for
    // location data
    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(app: Application): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(app)
    }
}

/**
1. provideWeatherApi: Creates a singleton instance of WeatherApi using Retrofit. The base URL is
set to "https://api.open-meteo.com/", and MoshiConverterFactory is used for JSON conversion.

2. provideFusedLocationProviderClient: Creates a singleton instance of FusedLocationProviderClient
using LocationServices. It takes an Application as a parameter.
*/