package com.example.ivr_call_app_v3.android.FunctionalComponents

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
fun scheduleservice(context: Context) {
    CoroutineScope(Dispatchers.Main).launch {
        Toast.makeText(context,"Scheduling Alarm",Toast.LENGTH_LONG).show()
    }
    val currentTime = Calendar.getInstance()
    currentTime.add(Calendar.MINUTE, 1)  // Set the alarm 1 minute ahead of the current time

    val delayMillis = currentTime.timeInMillis - System.currentTimeMillis()
    schedulealarm(context, delayMillis)
}


@SuppressLint("ScheduleExactAlarm")
fun schedulealarm(context: Context, delayMillis: Long) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, MyAlarmreceiver::class.java).apply {
        this.action = "hello"
    }
    val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

    alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delayMillis, pendingIntent)
}