package com.example.ivr_call_app_v3.android



import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.ivr_call_app_v3.Greeting
import com.example.ivr_call_app_v3.android.FunctionalComponents.AlarmComponents.AlarmConstants
import com.example.ivr_call_app_v3.android.FunctionalComponents.AlarmComponents.MyAlarmreceiver
import com.example.ivr_call_app_v3.android.UI.Main
import com.example.ivr_call_app_v3.android.UI.Splash


class MainActivity : ComponentActivity() {





    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Main()
                }
            }
        }
    }



}

@Composable
fun GreetingView(text: String) {
    Text(text = text)
}

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        GreetingView("Hello, Android!")
    }
}
