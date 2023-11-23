package com.khush.weatherapp.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.khush.weatherapp.domain.weather.WeatherData
import com.khush.weatherapp.presentation.MainActivity.Companion.degreeType
import java.math.RoundingMode
import java.time.format.DateTimeFormatter

// Composable function for displaying hourly weather information
@Composable
fun HourlyWeatherDisplay(
    // WeatherData object representing weather information for a specific hour
    weatherData: WeatherData,

    // Modifier for customizing the layout and behavior
    modifier: Modifier = Modifier,

    // Text color for the displayed information
    textColor: Color = Color.White
) {
    // Format the time using a 24-hour clock pattern
    val formattedTime = remember(weatherData) {
        weatherData.time.format(
            DateTimeFormatter.ofPattern("HH:mm")
        )
    }

    // Column to arrange child vertically
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Text displaying the formatted time
        Text(
            text = formattedTime,
            color = Color.LightGray
        )

        // Image displaying the weather icon
        Image(
            painter = painterResource(id = weatherData.weatherType.iconRes),
            contentDescription = null,
            modifier = Modifier.width(40.dp)
        )

        // Composite text displaying temperature in Celsius or Fahrenheit
        Text(
            text = initDegree(degreeType,weatherData.temperatureCelsius),
            color = textColor,
            fontWeight = FontWeight.Bold
        )
    }
}