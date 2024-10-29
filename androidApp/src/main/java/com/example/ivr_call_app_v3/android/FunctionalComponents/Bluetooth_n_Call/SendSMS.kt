package com.example.ivr_call_app_v3.android.FunctionalComponents.Bluetooth_n_Call

import android.content.Context
import android.telephony.SmsManager
import android.widget.Toast

fun sendSms(context: Context, phoneNumber: String, message: String) {
    // Check if the phone number and message are not empty
    if (phoneNumber.isNotEmpty() && message.isNotEmpty()) {
        // Get the SmsManager instance
        val smsManager = SmsManager.getDefault()

        try {
            // Send the SMS
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            Toast.makeText(context, "SMS sent successfully!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Failed to send SMS: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    } else {
        Toast.makeText(context, "Phone number or message cannot be empty.", Toast.LENGTH_SHORT).show()
    }
}
