package com.example.ivr_call_app_v3.android.UI.Navigation

import BluetoothViewModel
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ivr_call_app_v3.android.FunctionalComponents.Bluetooth_n_Call.BluetoothRepository
import com.example.ivr_call_app_v3.android.FunctionalComponents.Bluetooth_n_Call.CallStateRepository
import com.example.ivr_call_app_v3.android.FunctionalComponents.NetworkCalls.API_ViewModel
import com.example.ivr_call_app_v3.android.UI.BLEconnect
import com.example.ivr_call_app_v3.android.UI.Setup
import com.example.ivr_call_app_v3.android.UI.Splash

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyNavhostController(viewModel: API_ViewModel = androidx.lifecycle.viewmodel.compose.viewModel())
{
    val navHostController = rememberNavController()
    var context = LocalContext.current

    CallStateRepository.setup_viewmodel(viewModel)
    CallStateRepository.setup_sqlite(context)
    BluetoothRepository.myBluetooth = BluetoothViewModel(context)

    NavHost(navController = navHostController, startDestination = "splash" )
    {
        composable("splash")
        {
            Splash(navHostController)
//                Message()
        }

        composable("ipscreen")
        {
            var type = 0
            Setup(navHostController,viewModel,type)
        }

        composable("manualsetup")
        {
            var type = 1
            Setup(navHostController, viewModel, type)
        }

        composable("bleconnect")
        {
            BLEconnect(navHostController,viewModel)
        }
    }
}