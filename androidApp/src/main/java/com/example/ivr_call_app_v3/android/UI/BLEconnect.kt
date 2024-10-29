package com.example.ivr_call_app_v3.android.UI

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.ivr_call_app_v3.android.Constants.Constants
import com.example.ivr_call_app_v3.android.FunctionalComponents.AlarmComponents.AlarmConstants
import com.example.ivr_call_app_v3.android.FunctionalComponents.AlarmComponents.isServiceRunning
import com.example.ivr_call_app_v3.android.FunctionalComponents.AlarmComponents.scheduleservice
import com.example.ivr_call_app_v3.android.FunctionalComponents.Bluetooth_n_Call.CallStateRepository
import com.example.ivr_call_app_v3.android.FunctionalComponents.Bluetooth_n_Call.Call_SMS_Service
import com.example.ivr_call_app_v3.android.FunctionalComponents.NetworkCalls.API_ViewModel


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BLEconnect(navHostController: NavHostController, viewModel: API_ViewModel) {
    var patients = CallStateRepository.databaseHelper?.getAllPatients()
    Log.i("MyAlarmReceiver",patients.toString())
    var showBottomSheet1 by remember { mutableStateOf(false) }
    var showBottomSheet2 by remember { mutableStateOf(false) }
    var showBottomSheet3 by remember { mutableStateOf(false) }
    var issystemrunning by remember {
        mutableStateOf(false)
    }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )
    var isChecked by remember {
        mutableStateOf(false)
    }

    var servicerunning by remember {
        mutableStateOf(false)
    }
    var context = LocalContext.current

    if(isChecked && !issystemrunning)
    {
        Log.i("MyAlarmReceiver",CallStateRepository.databaseHelper?.getAllPatients().toString()+": Service : ${isServiceRunning(context,Call_SMS_Service::class.java)}")
        Log.i("MyAlarmReceiver",": Service : ${isServiceRunning(context,Call_SMS_Service::class.java)}")
            scheduleservice(context,AlarmConstants.START_ACTION)
        issystemrunning = true


    }
    else if (!isChecked && issystemrunning)
    {
        Log.i("MyAlarmReceiver",CallStateRepository.databaseHelper?.getAllPatients().toString()+": Service : ${isServiceRunning(context,Call_SMS_Service::class.java)}")
        Log.i("MyAlarmReceiver",": Service : ${isServiceRunning(context,Call_SMS_Service::class.java)}")
        scheduleservice(context, AlarmConstants.END_ACTION_PERMANENT)
        issystemrunning = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween, // Space between content and buttons
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Content at the top
        Box(modifier = Modifier.background(Color.Transparent).fillMaxWidth().wrapContentHeight(), contentAlignment = Alignment.TopEnd)
        {
            Button(
                onClick = { navHostController.navigate("manualsetup") }, colors = ButtonDefaults.buttonColors(
                    containerColor = Color(Constants.light),
                    contentColor = Color(Constants.dark)
                )
            ) {
                Text("Setup")
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .weight(1f), // Take up remaining space above the buttons
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header text
            Text(
                text = "Connect with Device",
                modifier = Modifier.padding(bottom = 20.dp), // Adjust padding
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Default
            )

            // ON/OFF Status Text
            Text(
                text = if (isChecked) "ON" else "OFF",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(30.dp)
            )

            // Switch component
            Switch(
                checked = isChecked,
                onCheckedChange = { isChecked = it },
                modifier = Modifier.scale(1.5f),
                thumbContent = {
                    if (isChecked) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "",
                            modifier = Modifier
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Filled.RadioButtonUnchecked,
                            contentDescription = "",
                            modifier = Modifier.size(40.dp)
                        )
                    }
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color(Constants.dark),
                    checkedBorderColor = Color(Constants.dark),
                    checkedTrackColor = Color(Constants.light),
                    checkedIconColor = Color(Constants.light),
                )
            )
        }

        // Spacer to push buttons to the bottom
//        Spacer(modifier = Modifier.weight(1f))

        // Buttons at the bottom
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { showBottomSheet1 = true }, colors = ButtonDefaults.buttonColors(
                    containerColor = Color(Constants.light),
                    contentColor = Color(Constants.dark)
                )
            ) {
                Text("Set Message")
            }

            Button(
                onClick = { showBottomSheet3 = true }, colors = ButtonDefaults.buttonColors(
                    containerColor = Color(Constants.light),
                    contentColor = Color(Constants.dark)
                )
            ) {
                Text("Set Timer & Admin")
            }

            Button(
                onClick = { showBottomSheet2 = true }, colors = ButtonDefaults.buttonColors(
                    containerColor = Color(Constants.light),
                    contentColor = Color(Constants.dark)
                )
            ) {
                Text("Connect Bluetooth")
            }
        }
    }



    if (showBottomSheet1) {
        Dialog(

            onDismissRequest = { showBottomSheet1 = false },
        ) {
            Message()
        }
    }

    if (showBottomSheet3) {
        Dialog(

            onDismissRequest = { showBottomSheet3 = false },
            ) {
            TimeSlots()
        }
//        ModalBottomSheet(
//            modifier = Modifier
//                .fillMaxHeight()
//                .background(Color.Transparent)
//            ,
//            sheetState = sheetState,
//            onDismissRequest = { showBottomSheet3 = false },
//            containerColor = Color.White,
//
//            ) {
//            TimeSlots()
//        }
    }

    if (showBottomSheet2) {
        ModalBottomSheet(
            modifier = Modifier
                .fillMaxHeight()
                .background(Color.Transparent),
            sheetState = sheetState,
            onDismissRequest = { showBottomSheet2 = false },
            containerColor = Color.White
        ) {
            BluetoothConnectionScreen()
        }
    }
}
