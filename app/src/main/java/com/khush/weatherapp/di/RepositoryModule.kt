package com.khush.weatherapp.di

import com.khush.weatherapp.data.repository.WeatherRepositoryImpl
import com.khush.weatherapp.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

// Dagger Hilt module for providing repository-related dependencies in the application scope
// Uses the ExperimentalCoroutinesApi annotation to indicate the experimental use of coroutines
@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    // Binds the WeatherRepositoryImpl implementation to the WeatherRepository interface
    // This allows Dagger Hilt to inject a WeatherRepository wherever it's required
    @Binds
    @Singleton
    abstract fun bindWeatherRepository(
        weatherRepositoryImpl: WeatherRepositoryImpl
    ): WeatherRepository
}

/**
The @Binds annotation is used to bind the WeatherRepositoryImpl implementation to
the WeatherRepository interface. This allows Dagger Hilt to inject a WeatherRepository
wherever it's required in the application
 */