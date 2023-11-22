package com.khush.weatherapp.presentation

import android.Manifest
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khush.weatherapp.presentation.ui.theme.DarkBlue
import com.khush.weatherapp.presentation.ui.theme.DeepBlue
import com.khush.weatherapp.presentation.ui.theme.WeatherAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: WeatherViewModel by viewModels()
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private var dayIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Request permissions
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            viewModel.loadWeatherInfo(0)
        }
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )
        )

        //Create UI
        setContent {
            var offsetY by remember { mutableStateOf(0f) }
            var offsetX by remember { mutableStateOf(0f) }

            WeatherAppTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectDragGestures { change, dragAmount ->
                                change.consume()

                                val (x, y) = dragAmount
                                when {
                                    x > 0 -> {
                                        if (offsetX > 50) {
                                            if (!isLoading) {
                                                //Log.d(TAG, offsetX.toString())
                                                if (dayIndex in 1..7) {
                                                    dayIndex -= 1
                                                }
                                                viewModel.updateWeatherInfo(dayIndex)
                                                isLoading = true
                                            }
                                        }
                                        offsetX = 0F
                                    }

                                    x < 0 -> {
                                        if (offsetX < -50) {
                                            if (!isLoading) {
                                                //Log.d(TAG, offsetX.toString())
                                                if (dayIndex in 0..5) {
                                                    dayIndex += 1
                                                }
                                                viewModel.updateWeatherInfo(dayIndex)
                                                isLoading = true
                                            }
                                        }
                                        offsetX = 0F
                                    }
                                }
                                when {
                                    y > 0 -> {
                                        if (offsetY > 30) {
                                            if (!isLoading) {
                                                viewModel.loadWeatherInfo(dayIndex)
                                                isLoading = true
                                                //Log.d(TAG, offsetY.toString())
                                            }
                                        }
                                        offsetY = 0F
                                    }

                                    y < 0 -> {}
                                }

                                offsetX += dragAmount.x
                                offsetY += dragAmount.y
                            }
                        }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(DarkBlue)
                    ) {
                        Spacer(modifier = Modifier.height(36.dp))
                        val city = viewModel.currentLocation?.let { getCityName(it.latitude, it.longitude) }
                        //Log.d(TAG, city?:"")
                        WeatherCard(
                            city?:"",
                            state = viewModel.state,
                            backgroundColor = DeepBlue
                        )
                        Spacer(modifier = Modifier.height(46.dp))
                        WeatherForecast(dayIndex, state = viewModel.state)
                    }
                    if (viewModel.state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    viewModel.state.error?.let { error ->
                        Text(
                            text = error,
                            color = Color.Red,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.align(Alignment.Center)
                        )
                        Button(
                            onClick = {
                                viewModel.loadWeatherInfo(0)
                            },
                            modifier = Modifier.align(Alignment.BottomCenter)
                                .padding(120.dp),
                            enabled = true,
                            border = BorderStroke(width = 1.dp, brush = SolidColor(Color.Gray)),
                            shape = MaterialTheme.shapes.medium
                        ){
                            Text(
                                text = "RELOAD",
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(6.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    private fun getCityName(lat: Double,long: Double): String {
        val geoCoder = Geocoder(this, Locale.getDefault())
        val address = geoCoder.getFromLocation(lat,long,1)
        return address?.get(0)?.adminArea ?: ""
    }

    companion object {
        const val TAG = "MyTag"
        var isLoading = false
    }
}

