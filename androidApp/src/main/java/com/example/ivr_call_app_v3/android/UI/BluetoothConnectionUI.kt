package com.example.ivr_call_app_v3.android.UI

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ivr_call_app_v3.android.Constants.Constants
import com.example.ivr_call_app_v3.android.FunctionalComponents.Bluetooth_n_Call.BluetoothRepository

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("MissingPermission")
@Composable
fun BluetoothConnectionScreen() {
    // Sample static content for demonstration
    val sampleDevices by BluetoothRepository.myBluetooth.pairedDevices.collectAsState()
    val sampleStatus by BluetoothRepository.myBluetooth.connectionStatus.collectAsState()
    var context = LocalContext.current

    LaunchedEffect(sampleStatus) {
        Toast.makeText(context,sampleStatus,Toast.LENGTH_LONG).show()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Title Text
            Text(
                text = "Bluetooth Connection",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp),
                color = Color.Black
            )

            // Scan Devices Button
            OutlinedButton(
                onClick = { BluetoothRepository.myBluetooth.fetchPairedDevices() },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor  = Color( Constants.light),
                    contentColor = Color(Constants.dark)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(text = "Scan Devices", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sample Device List
            if (sampleDevices.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(Constants.light)),
                    shape = RoundedCornerShape(16.dp),

                ) {
                    LazyColumn(modifier = Modifier.padding(16.dp)) {
                        items(sampleDevices) { item ->
                            Card(
                                modifier = Modifier
                                    .padding(vertical = 8.dp, horizontal = 16.dp)
                                    .fillMaxWidth()
                                    .wrapContentHeight(),
                                onClick = {BluetoothRepository.myBluetooth.connectToDevice(item)},
                                colors = CardDefaults.cardColors(containerColor = Color(Constants.dark), contentColor = Color(Constants.light)),
                                shape = RoundedCornerShape(12.dp),

                            ) {
                                Text(
                                    text = item.name,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Status Text
            Text(
                text = "Status: ${sampleStatus}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                textAlign = TextAlign.Center,
                color = Color.Black
            )
        }
    }
}

