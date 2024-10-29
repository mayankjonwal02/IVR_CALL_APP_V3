package com.example.ivr_call_app_v3.android.FunctionalComponents.Bluetooth_n_Call

import android.content.Context
import android.telecom.Call
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ivr_call_app_v3.android.FunctionalComponents.NetworkCalls.API_ViewModel
import com.example.ivr_call_app_v3.android.FunctionalComponents.NetworkCalls.Patient
import com.example.ivr_call_app_v3.android.FunctionalComponents.Storage.DatabaseHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object CallStateRepository
{
    val defaultsms = "Hey @name@, you have a @operationtype@ operation on @duedate@"
    val defaultStartHour = 9
    val defaultEndHour = 17
    val defaultStartMinute = 0
    val defaultEndMinute = 0
    val defaultAdminContact = "0000000000"


    private val _API_viewmodel = MutableStateFlow<API_ViewModel?>(null)
    val API_viewModel : StateFlow<API_ViewModel?> = _API_viewmodel

    var databaseHelper: DatabaseHelper? = null

    private val _callstate = MutableStateFlow<Int>(Call.STATE_DISCONNECTED)
    val callstate : StateFlow<Int> = _callstate

    private val _isCallServiceRunning = MutableStateFlow<Call?>(null)
    val isCallServiceRunning : StateFlow<Call?> = _isCallServiceRunning

    private val _contact = MutableStateFlow<String>("")
    val contact : StateFlow<String> = _contact


    private val _responce = MutableStateFlow<String>("")
    val responce : StateFlow<String> = _responce

    private val _disconnectionType = MutableStateFlow<Int>(0)
    val disconnectionType : StateFlow<Int> = _disconnectionType


    private val _counter = MutableStateFlow<Int>(0)
    val counter : StateFlow<Int> = _counter

    private val _currentpatient = MutableStateFlow<Patient?>(null)
    val currentpatient : StateFlow<Patient?> = _currentpatient

    fun updateCallState(currentstate : Int)
    {
        _callstate.value = currentstate

    }


    fun updateCallServiceState(current: Call?)
    {
        _isCallServiceRunning.value = current

    }

    fun updateCounterState(currentstate : Int)
    {
        _counter.value = currentstate

    }

    fun updatepatient(patient: Patient)
    {
        _currentpatient.value = patient
    }



    fun updateCallResponce(callresponce:String,disconnectionCode:Int)
    {
        _responce.value = callresponce
        _disconnectionType.value = disconnectionCode

    }

    fun setup_sqlite(context: Context)
    {
        if(databaseHelper == null)
        {
            databaseHelper = DatabaseHelper(context)
        }
    }
    fun setup_viewmodel(viewModel: API_ViewModel)
    {
        if(_API_viewmodel.value == null)
        {
            _API_viewmodel.value = viewModel
        }
    }
}