package com.khush.weatherapp.data.location

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.khush.weatherapp.domain.location.LocationTracker
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

@ExperimentalCoroutinesApi
class DefaultLocationTracker @Inject constructor(
    private val locationClient: FusedLocationProviderClient,
    private val application: Application
): LocationTracker {

    /**
     * Retrieves the client's current location asynchronously.
     *
     * @return The current location or null if the required permissions are not granted or if
     *         the location services are not enabled.
     */
    override suspend fun getCurrentLocation(): Location? {
        // Check if the app has fine and coarse location permissions
        val hasAccessFineLocationPermission = ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val hasAccessCoarseLocationPermission = ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        // Check if GPS or network location providers are enabled
        val locationManager = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        // Return null if permissions or location services are not available
        if(!hasAccessCoarseLocationPermission || !hasAccessFineLocationPermission || !isGpsEnabled) {
            return null
        }

        // Use coroutine to suspend and wait for location retrieval
        return suspendCancellableCoroutine { cont ->
            locationClient.lastLocation.apply {
                if(isComplete) {
                    if(isSuccessful) {
                        // Apply the location for future processing
                        cont.resume(result)
                    } else {
                        // Unable to retrieve location, return null
                        cont.resume(null)
                    }
                    return@suspendCancellableCoroutine
                }
                // Set up listeners for success, failure, and cancellation
                addOnSuccessListener {
                    cont.resume(it)
                }
                addOnFailureListener {
                    // Unable to retrieve location, return null
                    cont.resume(null)
                }
                addOnCanceledListener {
                    // Coroutine was canceled, cancel the continuation
                    cont.cancel()
                }
            }
        }
    }
}