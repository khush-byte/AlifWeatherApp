package com.khush.weatherapp.presentation

import android.Manifest
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.khush.weatherapp.presentation.ui.theme.DarkBlue
import com.khush.weatherapp.presentation.ui.theme.DeepBlue
import com.khush.weatherapp.presentation.ui.theme.WeatherAppTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: WeatherViewModel by viewModels()
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private var dayIndex = 0
    lateinit var locationList: ArrayList<Location>
    var cityLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.navigationBarColor = android.graphics.Color.BLACK
        }

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

        initListOfLocation()

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
                                                viewModel.updateWeatherInfo(
                                                    dayIndex,
                                                    cityLocation ?: locationList[0]
                                                )
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
                                                viewModel.updateWeatherInfo(
                                                    dayIndex,
                                                    cityLocation ?: locationList[0]
                                                )
                                                isLoading = true
                                            }
                                        }
                                        offsetX = 0F
                                    }
                                }
                                when {
                                    y > 0 -> {
                                        if (offsetY > 50) {
                                            if (!isLoading) {
                                                viewModel.loadWeatherByLocation(dayIndex, cityLocation ?: locationList[0])
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
                        Spacer(modifier = Modifier.height(4.dp))
                        val city = viewModel.currentLocation?.let {
                            getCityName(
                                it.latitude,
                                it.longitude
                            )
                        }
                        cityLocation = viewModel.currentLocation
                        CityDropDownList()
                        //Log.d(TAG, city?:"")
                        WeatherCard(
                            city ?: "",
                            state = viewModel.state,
                            backgroundColor = DeepBlue
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        WeatherForecast(dayIndex, state = viewModel.state)
                    }
                    if (viewModel.state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    viewModel.state.error?.let { error ->
                        Text(
                            text = "No internet connection or location is disabled.\nThe default location has been selected.\nPlease connect to the internet and reload.",
                            color = Color.Red,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.align(Alignment.Center)
                        )
                        Button(
                            onClick = {
                                //viewModel.loadWeatherInfo(0)
                                restart()
                            },
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(120.dp),
                            enabled = true,
                            border = BorderStroke(width = 1.dp, brush = SolidColor(Color.Gray)),
                            shape = MaterialTheme.shapes.medium
                        ) {
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

    private fun initListOfLocation() {
        locationList = ArrayList()

        val locDushanbe = Location("Dushanbe")
        locDushanbe.latitude = 38.577380
        locDushanbe.longitude = 68.735500
        locationList.add(locDushanbe)

        val locKhujand = Location("Khujand")
        locKhujand.latitude = 40.276346
        locKhujand.longitude = 69.635387
        locationList.add(locKhujand)

        val locKhatlon = Location("Sarband")
        locKhatlon.latitude = 37.726434
        locKhatlon.longitude = 68.988015
        locationList.add(locKhatlon)
    }

    private fun getCityName(lat: Double, long: Double): String {
        var cityName: String?
        val geoCoder = Geocoder(this, Locale.getDefault())
        val address = geoCoder.getFromLocation(lat, long, 1)
        cityName = address?.get(0)?.locality
        if (cityName == null) {
            cityName = address?.get(0)?.subAdminArea
            if (cityName == null) {
                cityName = address?.get(0)?.adminArea
            }
        }
        return cityName ?: ""
    }

    // Creating a composable function
    // to create an Outlined Text Field
    // Calling this function as content
    // in the above function
    @Composable
    fun CityDropDownList() {
        // Declaring a boolean value to store
        // the expanded state of the Text Field
        var mExpanded by remember { mutableStateOf(false) }

        // Create a list of cities
        val mCities = listOf(
            locationList[0].provider.toString(),
            locationList[1].provider.toString(),
            locationList[2].provider.toString()
        )

        // Create a string value to store the selected city
        var mSelectedText by remember { mutableStateOf("") }

        var mTextFieldSize by remember { mutableStateOf(Size.Zero) }

        // Up Icon when expanded and down icon when collapsed
        val icon = if (mExpanded)
            Icons.Filled.KeyboardArrowUp
        else
            Icons.Filled.KeyboardArrowDown

        Column(Modifier.padding(12.dp)) {

            // Create an Outlined Text Field
            // with icon and not expanded
            OutlinedTextField(
                value = mSelectedText,
                onValueChange = { mSelectedText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        // This value is used to assign to
                        // the DropDown the same width
                        mTextFieldSize = coordinates.size.toSize()
                    },
                label = { Text("Please select a city", color = Color.Gray) },
                enabled = false,
                trailingIcon = {
                    Icon(icon, "contentDescription",
                        Modifier.clickable { mExpanded = !mExpanded })
                }
            )

            // Create a drop-down menu with list of cities,
            // when clicked, set the Text Field text as the city selected
            DropdownMenu(
                expanded = mExpanded,
                onDismissRequest = { mExpanded = false },
                modifier = Modifier
                    .width(with(LocalDensity.current) { mTextFieldSize.width.toDp() })
            ) {
                mCities.forEach { label ->
                    DropdownMenuItem(onClick = {
                        mSelectedText = label
                        mExpanded = false
                        for (location in locationList) {
                            if (location.provider == mSelectedText) {
                                cityLocation = location
                                dayIndex = 0
                            }
                        }
                        //Log.d(TAG, locationList[0].provider ?: "")
                        viewModel.loadWeatherByLocation(0, cityLocation ?: locationList[0])
                    }) {
                        Text(text = label)
                    }
                }
            }
        }
    }

    private fun restart() {
        overridePendingTransition(0, 0)
        recreate()
        overridePendingTransition(0, 0)
    }

    companion object {
        const val TAG = "MyTag"
        var isLoading = false
        var degreeType = 1
    }
}

