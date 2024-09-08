package com.example.ivr_call_app_v3.android.UI

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ivr_call_app_v3.android.Constants.Constants

@Preview
@Composable
fun IPscreen()
{
    if (ApiViewmodel == null) {
        ApiViewmodel = viewModel()
    }

    var ipaddress by remember {
        mutableStateOf("")
    }

    LaunchedEffect(ipaddress) {
        if (ipaddress.isNotEmpty()) {
            ApiViewmodel?.initialize(ipaddress)
        }
    }

    val ConnectionMessage by ApiViewmodel!!.testconnection.collectAsState()
    val ErrorMessage by ApiViewmodel!!.errorMessage.collectAsState()
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.Transparent), verticalArrangement = Arrangement.Top)
    {
        Column(modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .background(Color.Transparent), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally)
        {
            Text(text = "Connect with Server", modifier = Modifier.padding(top = 60.dp), fontSize = 20.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, fontStyle = FontStyle.Normal)
            OutlinedTextField(
                value = ipaddress,
                onValueChange = { ipaddress = it },
                placeholder = {
                    Text(
//                        modifier = Modifier.fillMaxWidth(),
                        text = "Enter IP Address", // Add your placeholder text here
                        color = Color.Gray  ,
                        textAlign = TextAlign.Center// Placeholder text color,

                    )
                },
                modifier = Modifier.padding(top = 50.dp, start = 20.dp, end = 20.dp), textStyle = TextStyle(textAlign = TextAlign.Center),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color(Constants.dark),
                    focusedContainerColor = Color(Constants.light),
                    unfocusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color(Constants.light),
                    unfocusedTextColor = Color(Constants.dark),
                    focusedTextColor = Color(Constants.dark),
                    cursorColor = Color(Constants.dark)
                ),


            )
            Button(onClick = { ApiViewmodel!!.TestConnectionWithBackend() } , colors = ButtonDefaults.buttonColors(
                containerColor = Color(Constants.light),
                contentColor = Color(Constants.dark)
            ),
                modifier = Modifier.padding(top = 20.dp)) {
                Text(text = "Test Connection", fontWeight = FontWeight.Bold)
            }

            ConnectionMessage?.let {
                Text(text = "Message: ${it.message}", color = Color.Blue)
            }

            ErrorMessage?.let {
                Text(text = "Error: $it", color = Color.Red)
            }


        }

        Column(modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(Color.Transparent), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally)
        {


            Button(onClick = { /*TODO*/ } , colors = ButtonDefaults.buttonColors(
                containerColor = Color(Constants.light),
                contentColor = Color(Constants.dark)
            )) {
                Text(text = "Continue", fontWeight = FontWeight.Bold)
            }
        }
    }
}