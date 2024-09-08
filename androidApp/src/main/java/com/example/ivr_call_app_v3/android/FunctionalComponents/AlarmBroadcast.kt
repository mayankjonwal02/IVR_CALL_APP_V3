package com.example.ivr_call_app_v3.android.FunctionalComponents

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MyAlarmreceiver : BroadcastReceiver() {


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        Toast.makeText(context,"Broadcastreceived",Toast.LENGTH_LONG).show()
    }
}