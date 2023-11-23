package com.khush.weatherapp.presentation


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.RadioButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.khush.weatherapp.R
import com.khush.weatherapp.presentation.MainActivity.Companion.degreeType
import java.math.RoundingMode
import java.time.format.DateTimeFormatter

// Composable function to display the weather card
private var activity: MainActivity? = null
@Composable
fun WeatherCard(
    _activity: MainActivity,
    location: String,
    state: WeatherState,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    // Set the current activity
    activity = _activity

    // Set weather data if it is available
    state.weatherInfo?.currentWeatherData?.let { data ->

        // Display a card with weather information
        Card(
            backgroundColor = backgroundColor,
            shape = RoundedCornerShape(10.dp),
            modifier = modifier.padding(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // State for managing the display of a dialog box
                val showDialog = remember { mutableStateOf(false) }

                // Show dialog box if the state is true
                if (showDialog.value) {
                    DialogBox(
                        onDismiss = { showDialog.value = false }
                    )
                }
                // Date and settings button row
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                ) {
                    Text(
                        text = data.time.format(
                            DateTimeFormatter.ofPattern("EEEE, dd.M.yyyy")
                        ),
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(horizontal = 10.dp),
                        color = Color.White
                    )

                    // Button to open settings dialog
                    OutlinedButton(
                        colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Color.Transparent),
                        border = BorderStroke(0.dp, Color.Transparent),
                        modifier = Modifier.align(Alignment.BottomEnd),
                        onClick = {
                            showDialog.value = true
                        }
                    ) {
                        Icon(Icons.Default.Settings, "")
                    }
                }

                //Set space
                Spacer(modifier = Modifier.height(14.dp))

                // Weather icon and temperature
                Box(
                    modifier = Modifier.size(200.dp)
                ) {
                    Image(
                        painter = painterResource(id = data.weatherType.iconRes),
                        contentDescription = null,
                        modifier = Modifier.width(200.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = initDegree(degreeType, data.temperatureCelsius),
                    fontSize = 50.sp,
                    color = Color.White
                )

                // Weather description and location name
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = data.weatherType.weatherDesc,
                    fontSize = 20.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = location,
                    fontSize = 28.sp,
                    color = Color.White
                )

                // Navigation arrows icons
                Spacer(modifier = Modifier.height(16.dp))
                Box(modifier = Modifier.width(42.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_arrow_back),
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.BottomStart)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.baseline_arrow_forward),
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.BottomEnd)
                    )

                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

// Composable function to display a settings dialog box
@Composable
fun DialogBox(onDismiss: () -> Unit) {

    // Context for displaying a settings box
    val contextForToast = LocalContext.current.applicationContext

    Dialog(
        onDismissRequest = {
            onDismiss()
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            elevation = 4.dp
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Header with icon
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .background(color = Color(0xFF35898f)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        modifier = Modifier
                            .padding(top = 16.dp, bottom = 16.dp),
                        painter = painterResource(id = R.drawable.tempicon),
                        contentDescription = "degree",
                        alignment = Alignment.Center
                    )
                }

                // Text and degree unit options
                Text(
                    modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 12.dp),
                    text = "Choose the unit of measurement for temperature",
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))
                DegreeRadioButton()

                Spacer(modifier = Modifier.height(8.dp))

                // TextButton to apply selected degree type and dismiss the dialog
                TextButton(
                    onClick = {
                        // Dismiss the dialog
                        onDismiss()

                        // Apply the selected degree type by loading weather data with the updated parameters
                        activity!!.viewModel.loadWeatherByLocation(activity!!.dayIndex,
                            activity!!.cityLocation ?: activity!!.locationList[0], false)

//                        Toast.makeText(
//                            contextForToast,
//                            "Degree Type Applied",
//                            Toast.LENGTH_SHORT
//                        ).show()
                    }) {
                    Text(
                        // Apply button text with custom color
                        text = "Apply",
                        color = Color(0xFF35898f)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

// Composable function to display radio buttons for selecting degree unit
@Composable
fun DegreeRadioButton() {
    // State for tracking the selected degree option
    var selectedOption = remember { mutableStateOf("Option1") }

    // Set the selected option based on the current degree type
    if (degreeType == 2) {
        selectedOption = remember { mutableStateOf("Option2") }
    }

    // Column containing two degree unit options
    Column {
        // Celsius option
        Box(modifier = Modifier.width(140.dp)) {
            RadioButton(
                modifier = Modifier.align(Alignment.CenterStart),
                selected = selectedOption.value == "Option1",
                onClick = {
                    selectedOption.value = "Option1"
                    degreeType = 1
                }
            )
            Text(
                "Celsius", modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 42.dp)
            )
        }

        // Fahrenheit option
        Box(modifier = Modifier.width(140.dp)) {
            RadioButton(
                modifier = Modifier.align(Alignment.CenterStart),
                selected = selectedOption.value == "Option2",
                onClick = {
                    selectedOption.value = "Option2"
                    degreeType = 2
                }
            )
            Text(
                "Fahrenheit", modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 42.dp)
            )
        }
    }
}

// Function to format temperature based on the selected degree type
fun initDegree(type: Int, value: Double): String {
    // Convert temperature to Fahrenheit if the degree type is 2
    return if (type == 2) {
        val f: Double = (value * 9 / 5) + 32
        "${roundOffDecimal(f)}°F"
    } else {
        // Otherwise, use Celsius
        "$value°C"
    }
}

// Function to round off a decimal number
private fun roundOffDecimal(number: Double): Double {
    return number.toBigDecimal().setScale(1, RoundingMode.UP).toDouble()
}
