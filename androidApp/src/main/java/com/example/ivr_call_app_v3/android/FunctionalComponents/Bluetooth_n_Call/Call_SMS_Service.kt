package com.example.ivr_call_app_v3.android.FunctionalComponents.Bluetooth_n_Call

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.telecom.Call
import android.telecom.DisconnectCause
import android.util.Log
import androidx.compose.runtime.collectAsState
import com.example.ivr_call_app_v3.android.FunctionalComponents.NetworkCalls.Patient
import com.example.ivr_call_app_v3.android.FunctionalComponents.Storage.SharedPrefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class Call_SMS_Service : Service() {
    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        var API_viewmodel = CallStateRepository.API_viewModel

        serviceScope.launch {
            // Fetch patients and insert into the local database

            var oldpatients = CallStateRepository.databaseHelper?.getAllPatients()
            Log.d("SyncTrack","OLD PATIENTS FETCHED")
            Log.d("SyncTrack","old patients: ${oldpatients}")

            Log.d("SyncTrack","patients not empty")
            var deferredpatientlist = async { API_viewmodel.value?.UpdatePatientsToBackend() }
            var patientlist = deferredpatientlist.await()
            Log.d("SyncTrack","tried syncing data with database")
            Log.d("SyncTrack","returned List : ${patientlist}")

            var localpatients = patientlist

            if (localpatients != null && localpatients!!.isEmpty()) {
                Log.d("SyncTrack", "localpatients empty")
                var deferredUpdatedList = async{ API_viewmodel.value?.FetchPatientsFromBackend() }
                var updatedList = deferredUpdatedList.await()
                CallStateRepository.databaseHelper?.insertPatientsList(updatedList!!)
//                localpatients = API_viewmodel.value?.patients?.value
                Log.d("SyncTrack", "fetched data : ${updatedList}")

                Log.d("SyncTrack", "local patients fetched from backend ")
                Log.d("SyncTrack", "patients counter : ${CallStateRepository.counter.value}")
//                API_viewmodel.value?.UpdatePatientsToBackend()

                Log.d("SyncTrack", "--------------------------------------")



                        CallStateRepository.counter.collect { counter ->
                            Log.i("SyncTrack", "Collecting : counter value : ${counter}")
                            if (updatedList != null && counter < updatedList.size) {
                                val patient = updatedList[counter]
                                CallStateRepository.updatepatient(patient)
                                Log.i("Localpatient", patient.patientName)

                                // Initiate call
                                initiateCall(this@Call_SMS_Service, patient.patientCno)

                                // Delay to ensure call initiation before state collection
                                delay(2000)

                                // Process call state after initiating call

                            }
                        }


                CoroutineScope(Dispatchers.IO).launch {
                    CallStateRepository.callstate.collect { callstate ->
                        Log.i("Localpatient", "Call state: $callstate")
//                                    handleCallState(patient, callstate)  // Call your handler here
                    }
                }
            }
            else if (localpatients != null && localpatients!!.isNotEmpty())
            {
                Log.i("SyncTrack", "Data Wasn't Synced")

                            CallStateRepository.counter.collect { counter ->
                                Log.i("SyncTrack", "Collecting : counter value : ${counter}")
                                if (localpatients != null && counter < localpatients.size) {
                                    val patient = localpatients[counter]
                                    CallStateRepository.updatepatient(patient)
                                    Log.i("Localpatient", patient.patientName)

                                    // Initiate call
                                    var mymessage = SharedPrefs(this@Call_SMS_Service).getString("custommessage",CallStateRepository.defaultsms)
                                    mymessage = mymessage?.replace("@name@",patient.patientName, ignoreCase = true)
                                    mymessage = mymessage?.replace("@duedate@",patient.dueDate, ignoreCase = true)
                                    mymessage = mymessage?.replace("@operationtype@",patient.operationType, ignoreCase = true)
                                    sendSms(this@Call_SMS_Service,patient.patientCno,mymessage!!)

                                    initiateCall(this@Call_SMS_Service, patient.patientCno)

                                    // Delay to ensure call initiation before state collection
                                    delay(2000)

                                    // Process call state after initiating call

                                }
                            }

                            CoroutineScope(Dispatchers.IO).launch {
                                CallStateRepository.callstate.collect { callstate ->
                                    Log.i("Localpatient", "Call state: $callstate")
//                                    handleCallState(patient, callstate)  // Call your handler here
                                }
                            }

            }

        }


        return START_STICKY
    }

    private suspend fun handleCallState(patient: Patient, callstate: Int) {
//        if (callstate == Call.STATE_DISCONNECTED) {
            // Process disconnection cause and update patient details
            CallStateRepository.disconnectionType.collect { code ->
                when (code) {
                    DisconnectCause.BUSY -> Log.i("Localpatient", "Call was busy.")
                    DisconnectCause.REMOTE -> patient.numberOfInteractions += 1
                    DisconnectCause.LOCAL -> patient.numberOfInteractions += 1
                    else -> Log.i("Localpatient", "Call disconnected with code: $code")
                }
//            }

            // Update patient response
            CallStateRepository.responce.collect { callResponse ->
                patient.calledOn = getCurrentDate()
                val remainingDays = getDateDifference(patient.dueDate)
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

            // Reset call state after disconnection
//            resetCallState()
        }
    }

    // Reset the call state and other necessary fields for the next call
    private fun resetCallState() {
        CallStateRepository.updateCallState(Call.STATE_DISCONNECTED) // Reset call state
        CallStateRepository.updateCallResponce("", 0) // Reset response and disconnection type
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel() // Cancel the job to avoid memory leaks
        Log.d("Patients", "Service destroyed, updating patients")
        CallStateRepository.updateCounterState(0)

    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
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
        return TimeUnit.MILLISECONDS.toDays(diffInMillis).toInt()
    }
}
