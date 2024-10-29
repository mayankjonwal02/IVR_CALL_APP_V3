package com.example.ivr_call_app_v3.android.FunctionalComponents.Bluetooth_n_Call

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.telecom.TelecomManager
import android.widget.Toast

fun initiateCall(context: Context, phoneNumber: String) {
    // Check if the number is not empty
    if (phoneNumber.isNotEmpty()) {
        // Create a TelecomManager instance
        val telecomManager = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager

        // Create a Uri with the phone number
        val uri = Uri.parse("tel:$phoneNumber")

        // Check if we have the necessary permission to make calls
        if (context.checkSelfPermission(android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            // Initiate the call
            telecomManager.placeCall(uri, null)
        } else {
            // Permission not granted; you may want to request permission here
            Toast.makeText(context, "Permission to make calls is required.", Toast.LENGTH_SHORT).show()
        }
    } else {
        Toast.makeText(context, "Phone number cannot be empty.", Toast.LENGTH_SHORT).show()
    }
}
