package com.example.ivr_call_app_v3.android.FunctionalComponents.NetworkCalls

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

data class TestConnectionData(
    @SerializedName("message") val message : String
)
