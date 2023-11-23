package com.khush.weatherapp.presentation


import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonColors
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
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.khush.weatherapp.R
import com.khush.weatherapp.presentation.MainActivity.Companion.TAG
import com.khush.weatherapp.presentation.MainActivity.Companion.degreeType
import java.math.RoundingMode
import java.text.DecimalFormat
import java.time.format.DateTimeFormatter

@Composable
fun WeatherCard(
    location: String,
    state: WeatherState,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    state.weatherInfo?.currentWeatherData?.let { data ->
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
                val showDialog = remember { mutableStateOf(false) }
                if(showDialog.value) {
                    DialogBox(
                        onDismiss = {showDialog.value = false}
                    )
                }
                Box( modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)) {
                    Text(
                        text = data.time.format(
                            DateTimeFormatter.ofPattern("EEEE, dd.M.yyyy")
                        ),
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(horizontal = 10.dp),
                        color = Color.White
                    )
                    OutlinedButton(
                        colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Color.Transparent),
                        border = BorderStroke(0.dp, Color.Transparent),
                        modifier = Modifier.align(Alignment.BottomEnd),
                        onClick = {
                            showDialog.value = true
                        }
                    ) {
                        Icon(Icons.Default.Settings,"")
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
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
                    text = initDegree(degreeType,data.temperatureCelsius),
                    fontSize = 50.sp,
                    color = Color.White
                )
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

@Composable
fun DialogBox(onDismiss: () -> Unit) {
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

                Text(
                    modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 12.dp),
                    text = "Choose the unit of measurement for temperature",
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))
                DegreeRadioButton()
                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = {
                        onDismiss()
//                        Toast.makeText(
//                            contextForToast,
//                            "Degree Type Applied",
//                            Toast.LENGTH_SHORT
//                        ).show()
                    }) {
                    Text(
                        text = "Apply",
                        color = Color(0xFF35898f)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun DegreeRadioButton() {
    var selectedOption = remember { mutableStateOf("Option1") }

    if( degreeType == 2){
        selectedOption = remember { mutableStateOf("Option2") }
    }

    Column {
        Box( modifier = Modifier.width(140.dp)) {
            RadioButton(
                modifier = Modifier.align(Alignment.CenterStart),
                selected = selectedOption.value == "Option1",
                onClick = {
                    selectedOption.value = "Option1"
                    degreeType = 1
                }
            )
            Text("Celsius",  modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 42.dp))
        }
        Box( modifier = Modifier.width(140.dp)) {
            RadioButton(
                modifier = Modifier.align(Alignment.CenterStart),
                selected = selectedOption.value == "Option2",
                onClick = {
                    selectedOption.value = "Option2"
                    degreeType = 2
                }
            )
            Text("Fahrenheit",  modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 42.dp))
        }
    }
}

private fun initDegree(type: Int, value: Double): String{
    return if(type==2){
        val f: Double = (value * 9/5) + 32
        "${roundOffDecimal(f)}°F"
    }else{
        "$value°C"
    }
}

private fun roundOffDecimal(number: Double): Double {
    return number.toBigDecimal().setScale(1, RoundingMode.UP).toDouble()
}
