package com.example.ivr_call_app_v3.android.FunctionalComponents.Bluetooth_n_Call

import android.telecom.Call
import android.telecom.DisconnectCause
import android.telecom.InCallService
import android.util.Log
import android.widget.Toast
import com.example.ivr_call_app_v3.android.FunctionalComponents.NetworkCalls.Patient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

data class CatLan(var category: String, var language: String, var duedate: String)

class InCallService : InCallService() {

    private val callCallback = object : Call.Callback() {
        override fun onStateChanged(call: Call, state: Int) {
            when (state) {
                Call.STATE_DIALING -> handleDialingState(call)
                Call.STATE_CONNECTING -> handleConnectingState(call)
                Call.STATE_ACTIVE -> handleConnectedState(call)
                Call.STATE_DISCONNECTING -> handleDisconnectingState(call)
                Call.STATE_DISCONNECTED -> handleDisconnectedState(call)
                // Handle other states if needed
            }
        }
    }

    override fun onCallAdded(call: Call) {
        super.onCallAdded(call)
        CallStateRepository.updateCallServiceState(call)
        call.registerCallback(callCallback)
    }

    override fun onCallRemoved(call: Call) {
        CallStateRepository.updateCallServiceState(null)
        CallStateRepository.updateCounterState(CallStateRepository.counter.value + 1)
        super.onCallRemoved(call)
        call.unregisterCallback(callCallback)
    }

    private suspend fun showToast(message: String) {
        withContext(Dispatchers.Main) {
            Toast.makeText(this@InCallService, message, Toast.LENGTH_SHORT).show()
        }
    }



}


private fun handleDialingState(call: Call) {
    // User initiated the call, but it's still in the process of connecting
    CallStateRepository.updateCallState(Call.STATE_DIALING)

    Log.d("MyInCallService", "User initiated call")
}
private fun handleConnectingState(call: Call) {
    // User initiated the call, but it's still in the process of connecting
    CallStateRepository.updateCallState(Call.STATE_CONNECTING)
    Log.d("MyInCallService", "User initiated call, currently connecting.")
}

private fun handleConnectedState(call: Call) {
    // Call is connected
    CallStateRepository.updateCallState(Call.STATE_ACTIVE)
    var message = HashMap<String,String>()
    message["event"] = "start"
    message["category"] = CallStateRepository.currentpatient.value?.operationType?:"unknown"
    message["language"] = CallStateRepository.currentpatient.value?.language?:"unknown"
    BluetoothRepository.myBluetooth?.sendData(message)
    Log.d("MyInCallService", "Call is connected.")
}

private fun handleDisconnectingState(call: Call) {
    // This indicates that the user is disconnecting the call
    CallStateRepository.updateCallState(Call.STATE_DISCONNECTING)
    Log.d("MyInCallService", "User is disconnecting the call.")
}

private fun handleDisconnectedState(call: Call) {
    // Call has been disconnected
    Log.d("MyInCallService", "Call has been disconnected.")
    var currentpatient = CallStateRepository.currentpatient.value
    // You can check the cause of disconnection
    val disconnectCause = call.details.disconnectCause
    CoroutineScope(Dispatchers.IO).launch {
        if (currentpatient != null && disconnectCause != null) {
            handleCallState(currentpatient,disconnectCause)
        }
    }

    Log.d("MyInCallService", call.details.disconnectCause.toString())
    when (disconnectCause.code) {

        DisconnectCause.BUSY-> {
            CallStateRepository.updateCallResponce("Call Not Connected",DisconnectCause.BUSY)
            Log.d("MyInCallService", "Call disconnected . User Busy")
        }
        DisconnectCause.LOCAL -> {
            CallStateRepository.updateCallResponce("Perfectly Responded",DisconnectCause.LOCAL)
            Log.d("MyInCallService", "User disconnected the call.")
            // Handle user-initiated disconnection
        }
        DisconnectCause.REMOTE -> {
            CallStateRepository.updateCallResponce("Listened Half",DisconnectCause.REMOTE)
            var message = HashMap<String,String>()
            message["event"] = "stop"
            BluetoothRepository.myBluetooth?.sendData(message)
            Log.d("MyInCallService", "Call disconnected by the receiver.")
            // Handle disconnection by the receiver
        }
        else -> {
            CallStateRepository.updateCallResponce(call.details.disconnectCause?.reason!!,0)
            Log.d("MyInCallService", "Call disconnected due to an error.")
            // Handle error disconnection
        }
        // Handle other disconnect causes as needed
    }



}


private suspend fun handleCallState(patient: Patient, DisconnectionCause: DisconnectCause) {

    var callResponse = ""
    DisconnectionCause.code.let { code ->
        when (code) {
            DisconnectCause.BUSY -> {
                callResponse = "Call Not Connected"
                Log.i("Localpatient", "Call was busy.")
            }
            DisconnectCause.REMOTE -> {
                callResponse = "Listened Half"
                patient.numberOfInteractions += 1
            }
            DisconnectCause.LOCAL -> {
                callResponse = "Perfectly Responded"
                patient.numberOfInteractions += 1
            }
            else -> {
                callResponse = DisconnectionCause.reason
                Log.i("Localpatient", "Call disconnected with code: $code, Reason : ${callResponse}")
            }
        }
//            }

        // Update patient response

            patient.calledOn = getCurrentDate()
            val remainingDays = getDateDifference(patient.dueDate)
        Log.d("InCallPatientDateDifference","Calledon: ${patient.calledOn} , Duedate: ${patient.dueDate} , remainingdays: ${remainingDays}")
            when (8 - remainingDays) {
                1 -> patient.day1 = callResponse
                2 -> patient.day2 = callResponse
                3 -> patient.day3 = callResponse
                4 -> patient.day4 = callResponse
                5 -> patient.day5 = callResponse
                6 -> patient.day6 = callResponse
                7 -> patient.day7 = callResponse
            }
            CallStateRepository.databaseHelper?.updatePatientByEngagementId(patient)


    }
}

private fun getCurrentDate(): String {
    val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    return sdf.format(Date())
}

private fun getDateDifference(dateString: String): Int {
    val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val currentDate = Date()  // Get the current date
    val targetDate = sdf.parse(dateString)  // Parse the input date

    // Get the time difference in milliseconds
    val diffInMillis = targetDate?.time?.minus(currentDate.time) ?: 0

    // Convert milliseconds to days
    return TimeUnit.MILLISECONDS.toDays(diffInMillis).toInt() + 1
}

