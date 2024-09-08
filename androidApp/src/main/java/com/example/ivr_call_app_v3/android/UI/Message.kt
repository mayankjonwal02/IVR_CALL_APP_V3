package com.example.ivr_call_app_v3.android.UI

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ivr_call_app_v3.android.Constants.Constants

@Preview
@Composable
fun Message() {
    var message by remember { mutableStateOf("") }
    var starttime by remember { mutableStateOf(0) }
    var endtime by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .padding(16.dp) // General padding for the entire box
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title for Message Template
            Text(
                text = "Set Message Template",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(Constants.dark),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Message Input Field
            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                label = { Text("Enter Message") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            // Set Message Button
            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(Constants.light),
                    contentColor = Color(Constants.dark)
                ),
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(text = "Set Message", fontWeight = FontWeight.Bold)
            }

            // Title for Time Slot
            Text(
                text = "Set Time Slot",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(Constants.dark),
                modifier = Modifier.padding(top = 40.dp, bottom = 8.dp)
            )

            // Time Slot Input Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 16.dp)
            ) {
                // Start Time Input
                OutlinedTextField(
                    value = starttime.toString(),
                    onValueChange = { starttime = it.toIntOrNull() ?: 0 },
                    label = { Text("Start Time") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )

                // End Time Input
                OutlinedTextField(
                    value = endtime.toString(),
                    onValueChange = { endtime = it.toIntOrNull() ?: 0 },
                    label = { Text("End Time") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                )
            }

            // Set Time Slot Button
            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(Constants.light),
                    contentColor = Color(Constants.dark)
                ),
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(text = "Set Time Slot", fontWeight = FontWeight.Bold)
            }
        }
    }
}
