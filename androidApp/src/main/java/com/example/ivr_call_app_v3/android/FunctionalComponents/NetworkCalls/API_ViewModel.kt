package com.example.ivr_call_app_v3.android.FunctionalComponents.NetworkCalls

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class API_ViewModel : ViewModel()
{
    private var ApiInterface : API_Interface? = null

    private val _testconnection = MutableStateFlow<TestConnectionData?>(null)
    val testconnection : StateFlow<TestConnectionData?> = _testconnection

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage


    fun initialize(ipaddress : String)
    {
        val baseurl = "http://$ipaddress:5000/api/"
        val retrofit = RetrofitClient.getclient(baseurl)
        if (retrofit != null) {
            ApiInterface = retrofit.create(API_Interface::class.java)
        }
    }

    fun TestConnectionWithBackend()
    {
        _testconnection.value = null
        _errorMessage.value = null
        viewModelScope.launch{
            try {
                val responce = ApiInterface?.TestConnection()
                _testconnection.value = responce
            }
            catch (e: HttpException) {
                _errorMessage.value = "HttpException: ${e.message()}"
            } catch (e: Throwable) {
                _errorMessage.value = "Exception: ${e.message}"
            }
        }
    }

}