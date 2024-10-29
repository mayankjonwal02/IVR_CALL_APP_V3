package com.example.ivr_call_app_v3.android.FunctionalComponents.NetworkCalls

import com.google.gson.annotations.SerializedName

data class TestConnectionData(
    @SerializedName("message") val message : String
)

data class FetchTablesData(
    @SerializedName("message") val message : String,
    @SerializedName("error") val error : String,
    @SerializedName("tables") val tables : List<String>
)


data class FetchPatientsResponse(
    @SerializedName("message") val message: String,
    @SerializedName("patients") val patients: List<Patient>,
    @SerializedName("error") val error: String,
    @SerializedName("executed") val executed: Boolean,
)

data class Patient(
    @SerializedName("engagementid") val engagementId: String,
    @SerializedName("patientid") val patientId: String,
    @SerializedName("patientname") val patientName: String,
    @SerializedName("patientcno") val patientCno: String,
    @SerializedName("operationtype") val operationType: String,
    @SerializedName("language") val language: String,
    @SerializedName("calledon") var calledOn: String,
    @SerializedName("NumberOfInteractions") var numberOfInteractions: Int,
    @SerializedName("duedate") val dueDate: String,
    @SerializedName("day7") var day7: String,
    @SerializedName("day6") var day6: String,
    @SerializedName("day5") var day5: String,
    @SerializedName("day4") var day4: String,
    @SerializedName("day3") var day3: String,
    @SerializedName("day2") var day2: String,
    @SerializedName("day1") var day1: String
)

data class UpdatePatientsRequest(
    @SerializedName("patients") val patients: List<Patient>,
    @SerializedName("departmentname") val departmentName: String
)


data class UpdatePatientsResponce(
    @SerializedName("message") val message : String,
    @SerializedName("error") val error : String

)
data class FetchPatientsRequest(
    val departmentname: String
)

data class AddDepartmentRequestbody(
    val tableName: String
)


data class AddDepartmentResponcebody(
    @SerializedName("message") val message : String,
    @SerializedName("error") val error : String
)
