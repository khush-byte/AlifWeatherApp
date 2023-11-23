package com.khush.weatherapp.presentation

import android.location.Location
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khush.weatherapp.domain.location.LocationTracker
import com.khush.weatherapp.domain.repository.WeatherRepository
import com.khush.weatherapp.domain.util.Resource
import com.khush.weatherapp.presentation.MainActivity.Companion.TAG
import com.khush.weatherapp.presentation.MainActivity.Companion.isLoading
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

// HiltViewModel annotation indicates that this class is a ViewModel and should be injected by Hilt
@HiltViewModel
class WeatherViewModel @Inject constructor(
    // Injecting the required dependencies into the ViewModel
    private val repository: WeatherRepository,
    private val locationTracker: LocationTracker
) : ViewModel() {

    // Mutable state property to hold the current state of weather information
    var state by mutableStateOf(WeatherState())
        private set

    // Variable to store the current location
    var currentLocation: Location? = null

    // Function to load weather information based on the provided index
    fun loadWeatherInfo(index: Int) {
        viewModelScope.launch {
            // Updating the state to indicate loading and clear any previous errors
            state = state.copy(
                isLoading = true,
                error = null
            )

            // Attempting to retrieve the current location from the location tracker
            locationTracker.getCurrentLocation()?.let { location ->
                // Fetching weather data from the repository based on the location and index
                when (val result =
                    repository.getWeatherData(location.latitude, location.longitude, index)) {
                    is Resource.Success -> {
                        //Log.d(TAG, "${location.latitude}, ${location.longitude}")
                        // Adding a delay for illustration purposes (simulating network delay)
                        delay(2000L)

                        // Updating the state with the fetched weather information
                        state = state.copy(
                            weatherInfo = result.data,
                            isLoading = false,
                            error = null
                        )

                        // Updating the current location and indicating that loading has completed
                        isLoading = false
                        currentLocation = location
                    }

                    is Resource.Error -> {
                        // Updating the state in case of an error
                        state = state.copy(
                            weatherInfo = null,
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
            } ?: kotlin.run {
                // Updating the state in case the location couldn't be retrieved
                state = state.copy(
                    isLoading = false,
                    error = "Couldn't retrieve location. Make sure to grant permission and enable GPS."
                )
            }
        }
    }

    // Function to update weather information based on the provided index and location
    fun updateWeatherInfo(index: Int, location: Location) {
        viewModelScope.launch {
            viewModelScope.launch {
                // Updating the state to clear any previous errors
                state = state.copy(
                    isLoading = false,
                    error = null
                )

                // Fetching weather data from the repository based on the provided location and index
                when (val result =
                    repository.getWeatherData(location.latitude, location.longitude, index)) {
                    is Resource.Success -> {
                        state = state.copy(
                            weatherInfo = result.data,
                            isLoading = false,
                            error = null
                        )

                        // Updating the current location and indicating that loading has completed
                        isLoading = false
                        currentLocation = location
                    }

                    is Resource.Error -> {
                        // Updating the state in case of an error
                        state = state.copy(
                            weatherInfo = null,
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
            }
        }
    }

    // Function to load weather information based on the provided index and location, with an option to reload
    fun loadWeatherByLocation(index: Int, location: Location, reload: Boolean) {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true,
                error = null
            )

            when (val result =
                repository.getWeatherData(location.latitude, location.longitude, index)) {
                is Resource.Success -> {
                    // Adding a delay for illustration purposes (simulating network delay) when reloading
                    if(reload) {
                        delay(2000L)
                    }

                    // Updating the state with the fetched weather information
                    state = state.copy(
                        weatherInfo = result.data,
                        isLoading = false,
                        error = null
                    )
                    isLoading = false

                    // Updating the current location and indicating that loading has completed
                    currentLocation = location
                }

                is Resource.Error -> {
                    // Updating the state in case of an error
                    state = state.copy(
                        weatherInfo = null,
                        isLoading = false,
                        error = result.message
                    )
                }
            }
        }
    }
}