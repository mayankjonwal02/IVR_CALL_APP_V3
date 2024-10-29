package com.example.ivr_call_app_v3.android.FunctionalComponents.NetworkCalls

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ivr_call_app_v3.android.FunctionalComponents.Bluetooth_n_Call.CallStateRepository
import com.example.ivr_call_app_v3.android.FunctionalComponents.Storage.SharedPrefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class API_ViewModel(application: Application) : AndroidViewModel(application)
{
    private var ApiInterface : API_Interface? = null
    var sharedPrefs = SharedPrefs(application.applicationContext)
    private val _testconnection = MutableStateFlow<TestConnectionData?>(null)
    val testconnection : StateFlow<TestConnectionData?> = _testconnection

    private val _loading = MutableStateFlow<Boolean>(false)
    val loading : StateFlow<Boolean> = _loading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    private val _departmentmessage = MutableStateFlow<String?>(null)
    val departmentmessage: StateFlow<String?> = _departmentmessage


    fun initialize(ipaddress : String)
    {
        val baseurl = "http://$ipaddress:5000/api/"
        val retrofit = RetrofitClient.getclient(baseurl)
        if (retrofit != null) {
            ApiInterface = retrofit.create(API_Interface::class.java)
        }
        return
    }

    fun TestConnectionWithBackend()
    {
        _testconnection.value = null
        _errorMessage.value = null
        viewModelScope.launch{
            try {
                _loading.value = true
                val responce = ApiInterface?.TestConnection()
                _loading.value = false
                _testconnection.value = responce
                Log.e("Connecting", _testconnection.value.toString())
                Log.e("Connecting", _loading.value.toString())
                if(responce?.message == "Connection Successful")
                {
                    FetchDatabasesFromBackend()
                }
            }
            catch (e: HttpException) {
                _loading.value = false
                _errorMessage.value = "HttpException: ${e.message()}"
            } catch (e: Throwable) {
                _loading.value = false
                _errorMessage.value = "Exception: ${e.message}"
            }
        }
        return
    }

    private val _databases = MutableStateFlow<List<String>>(emptyList())
    val databases : StateFlow<List<String>> = _databases
    fun FetchDatabasesFromBackend()
    {
        _errorMessage.value = null
        viewModelScope.launch {
            try {
                val responce = ApiInterface?.FetchTables()
                if(responce?.error != null)
                {
                    _errorMessage.value = responce?.error
                }
                else
                {
                    _databases.value = responce!!.tables
                    Log.d("Databases",responce?.tables.toString())
                }
            }
            catch (e: HttpException) {

                _errorMessage.value = "HttpException: ${e.message()}"
            } catch (e: Throwable) {

                _errorMessage.value = "Exception: ${e.message}"
            }

        }
        return
    }


    private val _patients = MutableStateFlow<List<Patient>>(emptyList())
    val patients : StateFlow<List<Patient>> = _patients

    suspend fun FetchPatientsFromBackend(): List<Patient> {
        _errorMessage.value = ""

        return try {
            val fetchRequestBody = FetchPatientsRequest(
                departmentname = sharedPrefs.getString("department", "null")!!
            )

            // Make the API call in IO thread
            val response = withContext(Dispatchers.IO) {
                ApiInterface!!.FetchPatients(fetchRequestBody)
            }

            // Handle response
            if (response.error != null) {
                _errorMessage.value = response.error
                Log.d("Patients", _errorMessage.value.toString())
                emptyList()  // Return an empty list in case of error
            } else {
                _patients.value = response.patients
                Log.d("Patients", _patients.value.toString())
                response.patients  // Return the list of patients on success
            }
        } catch (e: HttpException) {
            _errorMessage.value = "HttpException: ${e.message()}"
            emptyList()  // Return an empty list on exception
        } catch (e: Throwable) {
            _errorMessage.value = "Exception: ${e.message}"
            emptyList()  // Return an empty list on exception
        }
    }


    suspend fun UpdatePatientsToBackend(): List<Patient>? {
        _errorMessage.value = ""
        _message.value = ""

        val patients = CallStateRepository.databaseHelper?.getAllPatients()

        // Ensure patients is not null before proceeding
        if (patients != null) {
            try {
                val updateRequestBody = UpdatePatientsRequest(
                    patients = patients,
                    departmentName = sharedPrefs.getString("department", "null")!!
                )

                // Make the API call and wait for the result
                Log.d("DataSync", "Patients : ${patients}")

                val response = ApiInterface?.updatePatients(updateRequestBody)
                Log.d("DataSync", response.toString())

                // Handle response
                return if (response?.error == null) {
                    CallStateRepository.databaseHelper?.clearPatients()
                    emptyList() // Return empty list on success
                } else {
                    _errorMessage.value = response?.error
                    Log.d("Patients", _errorMessage.value.toString())
                    null
                }

            } catch (e: HttpException) {
                _errorMessage.value = "HttpException: ${e.message()}"
            } catch (e: Throwable) {
                _errorMessage.value = "Exception: ${e.message}"
            }
        }

        return patients // Return the original list if not successful
    }


    suspend fun AddDepartmenttoBackend(departmentname:String) : String?
    {
        var requestbody = AddDepartmentRequestbody(tableName = departmentname)

        try {
            _loading.value = true
            var responce = ApiInterface?.AddDepartment(requestbody)

            if(responce!!.error == null)
            {
                _loading.value = false
                return responce.message
            }
            else
            {
                return responce.error
            }
        }
        catch (e: HttpException) {
            Log.i("exception",e.message.toString())
            return "HttpException: ${e.message()}"
        } catch (e: Throwable) {
           return  "Exception: ${e.message}"
        }

        return null

    }



}