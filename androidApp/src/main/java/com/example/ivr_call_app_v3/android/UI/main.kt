package com.example.ivr_call_app_v3.android.UI

import TestBroadcastUI
import android.app.Activity
import android.os.Build
import android.view.Window
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import com.example.ivr_call_app_v3.android.Constants.Constants
import com.example.ivr_call_app_v3.android.FunctionalComponents.NetworkCalls.API_ViewModel
import com.example.ivr_call_app_v3.android.UI.Navigation.MyNavhostController



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Main()
{
    var view = LocalView.current
    var window = (view.context as Activity).window
    window.navigationBarColor = Color(Constants.dark).toArgb()
    window.statusBarColor = Color(Constants.dark).toArgb()

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
        MyNavhostController()

//        BLEconnect()
//        Test1()
//        TestBroadcastUI()
    }
}