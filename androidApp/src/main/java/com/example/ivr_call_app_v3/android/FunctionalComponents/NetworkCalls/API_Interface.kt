package com.example.ivr_call_app_v3.android.FunctionalComponents.NetworkCalls

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface API_Interface {

    @GET("testconnection")
    suspend fun TestConnection() : TestConnectionData

    @GET("fetchTables")
    suspend fun FetchTables() : FetchTablesData

    @POST("fetchPatients")
    suspend fun FetchPatients(@Body request: FetchPatientsRequest): FetchPatientsResponse

    @POST("updatePatients")
    suspend fun updatePatients(@Body request: UpdatePatientsRequest): UpdatePatientsResponce

    @POST("createTable")
    suspend fun AddDepartment(@Body request : AddDepartmentRequestbody) : AddDepartmentResponcebody


}