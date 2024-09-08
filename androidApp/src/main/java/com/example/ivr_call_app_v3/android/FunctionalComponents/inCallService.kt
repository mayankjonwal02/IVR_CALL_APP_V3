package com.example.ivr_call_app_v3.android.FunctionalComponents

import android.telecom.Call
import android.telecom.InCallService
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class CatLan(var category: String, var language: String, var duedate: String)

class InCallService : InCallService() {

    override fun onCreate() {
        super.onCreate()
    }

    override fun onCallAdded(call: Call?) {
        super.onCallAdded(call)

        call?.registerCallback(object : Call.Callback() {
            override fun onStateChanged(call: Call?, state: Int) {
                super.onStateChanged(call, state)

                CoroutineScope(Dispatchers.IO).launch {
                    when (state) {
                        Call.STATE_DIALING -> {
                            showToast("Dialing ${call?.details?.handle?.schemeSpecificPart}")
                        }
                        Call.STATE_ACTIVE -> {
                            showToast("Call Active")
                        }
                        Call.STATE_DISCONNECTED -> {
                            showToast("Call Disconnected")
                        }
                        else -> {
                            showToast("Call State Changed: $state")
                        }
                    }
                }
            }
        })
    }

    override fun onCallRemoved(call: Call?) {
        super.onCallRemoved(call)
        CoroutineScope(Dispatchers.IO).launch {
            showToast("Call Removed")
        }
    }

    private suspend fun showToast(message: String) {
        withContext(Dispatchers.Main) {
            Toast.makeText(this@InCallService, message, Toast.LENGTH_SHORT).show()
        }
    }
}
