package com.example.ivr_call_app_v3.android.UI

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ivr_call_app_v3.android.Constants.Constants

@Composable
fun BLEconnect() {
    var isChecked by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        Column(modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .background(Color.Transparent), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally)
        {
            Text(
                text = "Connect with Device",
                modifier = Modifier.padding(top = 60.dp),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Default,
                fontStyle = FontStyle.Normal
            )
        }
            Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = if (isChecked) "ON" else "OFF",modifier = Modifier.padding( 30.dp), fontSize = 30.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Default, fontStyle = FontStyle.Normal)

            Switch(
                checked = isChecked,
                onCheckedChange = { isChecked = it },
                modifier = Modifier.scale(1.5f),
                thumbContent = {
                    if(isChecked) {
                        Icon(imageVector = Icons.Filled.Check, contentDescription = "", modifier = Modifier)
                    } else {
                        Icon(imageVector = Icons.Filled.RadioButtonUnchecked, contentDescription = "" , modifier = Modifier.size(40.dp))
                    }
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color(Constants.dark),
                    checkedBorderColor = Color(Constants.dark),
                    checkedTrackColor = Color(Constants.light),
                    checkedIconColor = Color(Constants.light),
                )

            )
                Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
