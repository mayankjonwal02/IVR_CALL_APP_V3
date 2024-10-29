package com.example.ivr_call_app_v3.android.UI

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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

@Preview
@Composable
fun Message() {
    val isImeVisible by rememberImeState()
    var context = LocalContext.current
    var sharedpref = SharedPrefs(context)
    var message by remember { mutableStateOf(sharedpref.getString("custommessage", CallStateRepository.defaultsms)) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color.Transparent)
//            .imePadding() // Padding when keyboard is visible
            .padding(16.dp) // General padding for the entire box,
        ,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier

                .background(Color.Transparent)
                .verticalScroll(rememberScrollState()), // Scrollable when content overflows
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))

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
                value = message!!,
                onValueChange = { message = it },
                label = { Text("Enter Message") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = "Instructions:", color = Color.Black, fontWeight = FontWeight.ExtraBold)
                Text(text = "write @name@ for name", color = Color.Black, fontWeight = FontWeight.ExtraBold)
                Text(text = "write @operationtype@ for operation type", color = Color.Black, fontWeight = FontWeight.ExtraBold)
                Text(text = "write @duedate@ for date", color = Color.Black, fontWeight = FontWeight.ExtraBold)
            }

            // Set Message Button
            Spacer(modifier = Modifier.height(40.dp))
            Button(
                onClick = {
                    message?.let {
                        if (it.isNotEmpty()) {
                            sharedpref.add("custommessage", it)
                            Toast.makeText(context, "SMS Template Saved", Toast.LENGTH_LONG).show()
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(Constants.light),
                    contentColor = Color(Constants.dark)
                ),
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(text = "Set Message", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(30.dp))

            // Title for Time Slot



        }
    }
}




