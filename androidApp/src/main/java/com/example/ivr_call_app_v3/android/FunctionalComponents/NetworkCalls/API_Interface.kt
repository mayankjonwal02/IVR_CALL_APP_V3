package com.example.ivr_call_app_v3.android.FunctionalComponents.NetworkCalls

import retrofit2.http.GET

interface API_Interface {

    @GET("testconnection")
    suspend fun TestConnection() : TestConnectionData

}