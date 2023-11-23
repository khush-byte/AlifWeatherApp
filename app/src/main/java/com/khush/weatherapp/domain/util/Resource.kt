package com.khush.weatherapp.domain.util

// A sealed class representing a generic resource that can be either Success or Error
sealed class Resource<T>(val data: T? = null, val message: String? = null) {

    // Represents a successful resource with optional associated data
    class Success<T>(data: T?): Resource<T>(data)

    // Represents an error resource with an associated error message and optional data
    class Error<T>(message: String, data: T? = null): Resource<T>(data, message)
}
