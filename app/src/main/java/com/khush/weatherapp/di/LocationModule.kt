package com.khush.weatherapp.di

import com.khush.weatherapp.data.location.DefaultLocationTracker
import com.khush.weatherapp.domain.location.LocationTracker
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

// Dagger Hilt module for providing location-related dependencies in the application scope
// Uses the ExperimentalCoroutinesApi annotation to indicate the experimental use of coroutines
@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
abstract class LocationModule {

    // Binds the DefaultLocationTracker implementation to the LocationTracker interface
    // This allows Dagger Hilt to inject a LocationTracker wherever it's required
    @Binds
    @Singleton
    abstract fun bindLocationTracker(defaultLocationTracker: DefaultLocationTracker): LocationTracker
}