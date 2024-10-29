package com.example.ivr_call_app_v3.android.UI



import android.app.TimePickerDialog
import android.widget.TimePicker
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ivr_call_app_v3.android.Constants.Constants
import com.example.ivr_call_app_v3.android.FunctionalComponents.Bluetooth_n_Call.CallStateRepository
import com.example.ivr_call_app_v3.android.FunctionalComponents.Storage.SharedPrefs
import com.example.ivr_call_app_v3.android.UI.Components.rememberImeState
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeSlots() {
    val context = LocalContext.current
    val sharedPrefs = SharedPrefs(context)

    // Load saved Start and End times from SharedPrefs
    var startHour by remember { mutableStateOf(sharedPrefs.getInt("starthour", CallStateRepository.defaultStartHour)) }
    var startMinute by remember { mutableStateOf(sharedPrefs.getInt("startminute", CallStateRepository.defaultStartMinute)) }
    var endHour by remember { mutableStateOf(sharedPrefs.getInt("endhour", CallStateRepository.defaultEndHour)) }
    var endMinute by remember { mutableStateOf(sharedPrefs.getInt("endminute", CallStateRepository.defaultEndMinute)) }

    var showStartTimeDialog by remember { mutableStateOf(false) }
    var showEndTimeDialog by remember { mutableStateOf(false) }

    var adminContact by remember {
        mutableStateOf(sharedPrefs.getString("admincontact",CallStateRepository.defaultAdminContact))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Time Slot Input Fields
            OutlinedButton(
                onClick = { showStartTimeDialog = true },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    ,
                colors = ButtonDefaults.outlinedButtonColors(

                    contentColor = Color.Blue
                ),
                border = BorderStroke(2.dp, Color.Blue),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = "Start Time: ${String.format("%02d:%02d", startHour, startMinute)}")
            }

            OutlinedButton(
                onClick = { showEndTimeDialog = true },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                ,
                colors = ButtonDefaults.outlinedButtonColors(

                    contentColor = Color.Blue
                ),
                border = BorderStroke(2.dp, Color.Blue),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = "End Time: ${String.format("%02d:%02d", endHour, endMinute)}")
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Instructions:",
                    color = Color.Black,
                    fontWeight = FontWeight.Light
                )
                Text(
                    text = "Enter Time in 24-Hour Time Format",
                    color = Color.Black,
                    fontWeight = FontWeight.Light
                )
            }



            // Save Button
            Button(
                onClick = {
                    if (endHour!! > startHour!! || (endHour!! == startHour!! && endMinute!! > startMinute!!)) {
                        sharedPrefs.addInt("starthour", startHour!!)
                        sharedPrefs.addInt("startminute", startMinute!!)
                        sharedPrefs.addInt("endhour", endHour!!)
                        sharedPrefs.addInt("endminute", endMinute!!)
                        Toast.makeText(context, "Time Slot Saved", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, "Invalid Time Slot", Toast.LENGTH_LONG).show()
                    }
                },
                modifier = Modifier.padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(Constants.dark),
                    contentColor = Color(Constants.light)
                )
            ) {
                Text(text = "Save Time Slot")
            }
            Spacer(modifier = Modifier.height(30.dp))
            adminContact?.let {
                OutlinedTextField(
                    value = it,
                    onValueChange = { adminContact = it },
                    label = { Text(text = "Admin's Contact")},
                    leadingIcon = { Icon(imageVector = Icons.Filled.Call, contentDescription = "", tint = Color(Constants.dark))},
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.White,
                        focusedTextColor = Color(Constants.dark),
                        unfocusedTextColor = Color.Gray,
                        focusedBorderColor = Color(Constants.dark),
                        unfocusedBorderColor = Color.Gray,
                        focusedPlaceholderColor = Color.Gray,
                        unfocusedPlaceholderColor = Color.Gray,
                        cursorColor = Color.Black,
                        focusedLabelColor = Color(Constants.dark)
                    )
                    )

            }

            Button(
                onClick = {
                    if (adminContact != CallStateRepository.defaultAdminContact && adminContact!!.length.equals(10)) {
                        adminContact?.let { sharedPrefs.add("admincontact", it) }
                        Toast.makeText(context, "Admin's Contact Saved", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, "Invalid Contact", Toast.LENGTH_LONG).show()
                    }
                },
                modifier = Modifier.padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(Constants.dark),
                    contentColor = Color(Constants.light)
                )
            ) {
                Text(text = "Save Contact")
            }
        }
    }

    // Start Time Picker Dialog
    if (showStartTimeDialog) {
        TimePickerDialogExample(
            initialHour = startHour!!,
            initialMinute = startMinute!!,
            onConfirm = { hour, minute ->
                startHour = hour
                startMinute = minute
                showStartTimeDialog = false
            },
            onDismiss = { showStartTimeDialog = false }
        )
    }

    // End Time Picker Dialog
    if (showEndTimeDialog) {
        TimePickerDialogExample(
            initialHour = endHour!!,
            initialMinute = endMinute!!,
            onConfirm = { hour, minute ->
                endHour = hour
                endMinute = minute
                showEndTimeDialog = false
            },
            onDismiss = { showEndTimeDialog = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialogExample(
    initialHour: Int,
    initialMinute: Int,
    onConfirm: (Int, Int) -> Unit,
    onDismiss: () -> Unit
) {
    val timePickerState = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = true
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                onConfirm(timePickerState.hour, timePickerState.minute)
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        text = {
            TimePicker(state = timePickerState)
        }
    )
}

