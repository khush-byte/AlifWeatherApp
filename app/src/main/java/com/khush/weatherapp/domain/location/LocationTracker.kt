package com.khush.weatherapp.domain.location

import android.location.Location

// Interface defining the contract for obtaining the current location
interface LocationTracker {

    // A suspend function that returns the current location or null if not available
    suspend fun getCurrentLocation(): Location?
}