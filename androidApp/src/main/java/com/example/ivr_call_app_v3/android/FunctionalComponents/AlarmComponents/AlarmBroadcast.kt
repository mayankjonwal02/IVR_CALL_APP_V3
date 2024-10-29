package com.example.ivr_call_app_v3.android.FunctionalComponents.AlarmComponents

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.ivr_call_app_v3.android.FunctionalComponents.Bluetooth_n_Call.CallStateRepository
import com.example.ivr_call_app_v3.android.FunctionalComponents.Bluetooth_n_Call.Call_SMS_Service
import com.example.ivr_call_app_v3.android.FunctionalComponents.Bluetooth_n_Call.sendSms
import com.example.ivr_call_app_v3.android.FunctionalComponents.Storage.SharedPrefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyAlarmreceiver : BroadcastReceiver() {


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        var contact = SharedPrefs(context!!).getString(
            "admincontact",
            CallStateRepository.defaultAdminContact
        )
        var viewModel = CallStateRepository.API_viewModel.value
        var dbhelper = CallStateRepository.databaseHelper

        var isnotcontact = contact.equals(CallStateRepository.defaultAdminContact)

        if (action == AlarmConstants.START_ACTION) {
            if (!isServiceRunning(context!!, Call_SMS_Service::class.java)) {
                Toast.makeText(context, "Starting IVR System", Toast.LENGTH_SHORT).show()

                val startServiceIntent = Intent(context, Call_SMS_Service::class.java)
                context.startService(startServiceIntent)
                if (!isnotcontact) {
                    if (contact != null) {

                        sendSms(context, contact, AlarmConstants.ADMIN_MESSAGE_START)
                    }

                }

                scheduleservice(context, "ACTION_STOP_SERVICE")
            }
            else if (action == AlarmConstants.END_ACTION)
            {

                if (isServiceRunning(context, Call_SMS_Service::class.java)) {
                    Toast.makeText(context, "Pausing IVR System", Toast.LENGTH_SHORT).show()
                    var patients = dbhelper!!.getAllPatients()
                    if(patients!!.isNotEmpty())
                    {
                        try {
                            CoroutineScope(Dispatchers.IO).launch {
                                viewModel!!.UpdatePatientsToBackend()
                            }

                        }
                        catch (e:Exception)
                        {
                            Log.e("DataSync","Error : ${e}")
                        }
                    }



                    val stopServiceIntent = Intent(context, Call_SMS_Service::class.java)
                    context.stopService(stopServiceIntent)
                    if (!isnotcontact) {
                        if (contact != null) {
                            sendSms(context, contact, AlarmConstants.ADMIN_MESSAGE_STOP)
                        }

                    }
                }

                scheduleservice(context, "ACTION_START_SERVICE")
            }
            else if(action == "test")
            {
                Toast.makeText(context,"Received Test Intent",Toast.LENGTH_LONG).show()
                Log.d("MyAlarmReceiver", "onReceive called with action: $action")


            }
            else if (action == AlarmConstants.END_ACTION_PERMANENT)
            {
                Toast.makeText(context,"Received Permanent Stop",Toast.LENGTH_LONG).show()
                if (isServiceRunning(context!!, Call_SMS_Service::class.java)) {
                    Toast.makeText(context, "Stopping IVR System", Toast.LENGTH_SHORT).show()
                    var patients = dbhelper!!.getAllPatients()
                    if(patients!!.isNotEmpty())
                    {
                        try {
                            CoroutineScope(Dispatchers.IO).launch {
                                viewModel!!.UpdatePatientsToBackend()
                            }
                        }
                        catch (e:Exception)
                        {
                            Log.e("DataSync","Error : ${e}")
                        }
                    }
                    val stopServiceIntent = Intent(context, Call_SMS_Service::class.java)
                    context.stopService(stopServiceIntent)

                    if (!isnotcontact) {
                        if (contact != null) {
                            sendSms(context, contact, AlarmConstants.ADMIN_MESSAGE_STOP_PERMANENT)
                        }

                    }
                }
            }
        }
    }
}