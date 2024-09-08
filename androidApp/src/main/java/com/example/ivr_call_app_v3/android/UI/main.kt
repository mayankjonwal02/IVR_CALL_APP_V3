package com.example.ivr_call_app_v3.android.UI

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ivr_call_app_v3.android.Constants.Constants
import com.example.ivr_call_app_v3.android.FunctionalComponents.NetworkCalls.API_ViewModel


var ApiViewmodel : API_ViewModel? = null
@Composable
fun Main()
{
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(

                        Color(Constants.dark),
                        Color(Constants.light),
                        Color(Constants.dark),

                        )
                )
            )
    )
    {
//        Splash()
        IPscreen()
//        BLEconnect()
//        Test1()
    }
}