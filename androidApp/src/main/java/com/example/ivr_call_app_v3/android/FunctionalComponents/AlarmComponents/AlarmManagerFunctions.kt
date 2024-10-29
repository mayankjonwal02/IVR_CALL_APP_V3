package com.example.ivr_call_app_v3.android.FunctionalComponents.AlarmComponents

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.ivr_call_app_v3.android.FunctionalComponents.Bluetooth_n_Call.CallStateRepository
import com.example.ivr_call_app_v3.android.FunctionalComponents.Bluetooth_n_Call.Call_SMS_Service
import com.example.ivr_call_app_v3.android.FunctionalComponents.Bluetooth_n_Call.sendSms
import com.example.ivr_call_app_v3.android.FunctionalComponents.NetworkCalls.API_ViewModel
import com.example.ivr_call_app_v3.android.FunctionalComponents.Storage.SharedPrefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
fun scheduleservice(context: Context, action: String) {

    Toast.makeText(context,"received a call",Toast.LENGTH_LONG).show()
    Log.i("MyAlarmReceiver","Call Received")
    var starthour = SharedPrefs(context).getInt("starthour",CallStateRepository.defaultStartHour)
    var startminute = SharedPrefs(context).getInt("startminute",CallStateRepository.defaultStartMinute)
    var endhour = SharedPrefs(context).getInt("endhour",CallStateRepository.defaultEndHour)
    var endminute = SharedPrefs(context).getInt("endminute",CallStateRepository.defaultEndMinute)

    var startTime = LocalTime.of(starthour!!,startminute!!,0)
    var endTime = LocalTime.of(endhour!!,endminute!!,0)

    val desiredtime = if(action == AlarmConstants.START_ACTION)
    {
        Log.i("MyAlarmReceiver","Call to start")
        startTime
    }
    else if (action == AlarmConstants.END_ACTION)
    {
        Log.i("MyAlarmReceiver","Call to pause")
        endTime
    }
    else
    {
        Log.i("MyAlarmReceiver","Call to end")
        LocalTime.now()
    }

    var schedulingtime  = Calendar.getInstance()
    val currenttime = LocalTime.now()
    var startdelaymillis = 0L
    if(action == AlarmConstants.START_ACTION)
    {
        if(currenttime.isBefore(desiredtime))
        {
            Log.i("MyAlarmReceiver","start time is before")
            schedulingtime.set(Calendar.HOUR_OF_DAY,desiredtime.hour)
            schedulingtime.set(Calendar.MINUTE,desiredtime.minute)
            schedulingtime.set(Calendar.SECOND,desiredtime.second)
        }
        else if ((currenttime.isAfter(desiredtime) or currenttime.equals(desiredtime)) and currenttime.isBefore(endTime))
        {
            Log.i("MyAlarmReceiver","start time is between")
            schedulingtime.set(Calendar.HOUR_OF_DAY,currenttime.hour)
            schedulingtime.set(Calendar.MINUTE,currenttime.minute )
            schedulingtime.set(Calendar.SECOND,currenttime.second)
        }
        else if(currenttime.isAfter(endTime))
        {
            Log.i("MyAlarmReceiver","start time is after")
            schedulingtime.add(Calendar.DAY_OF_YEAR,1)
            schedulingtime.set(Calendar.HOUR_OF_DAY,desiredtime.hour)
            schedulingtime.set(Calendar.MINUTE,desiredtime.minute)
            schedulingtime.set(Calendar.SECOND,desiredtime.second)
        }
    }
    else if(action == AlarmConstants.END_ACTION)
    {
        if(currenttime.isBefore(desiredtime))
        {
            Log.i("MyAlarmReceiver","pausetime is before")
            schedulingtime.set(Calendar.HOUR_OF_DAY,desiredtime.hour)
            schedulingtime.set(Calendar.MINUTE,desiredtime.minute)
            schedulingtime.set(Calendar.SECOND,desiredtime.second)
        }
        else
        {
            Log.i("MyAlarmReceiver","pausetime is after")
            schedulingtime.set(Calendar.HOUR_OF_DAY,currenttime.hour)
            schedulingtime.set(Calendar.MINUTE,currenttime.minute+1)
            schedulingtime.set(Calendar.SECOND,currenttime.second)
        }
    }
    else if(action == AlarmConstants.END_ACTION_PERMANENT)
    {
        Log.i("MyAlarmReceiver","endtime is now")
        schedulingtime.set(Calendar.HOUR_OF_DAY,currenttime.hour)
        schedulingtime.set(Calendar.MINUTE,currenttime.minute)
        schedulingtime.set(Calendar.SECOND,currenttime.second)
    }

    startdelaymillis = schedulingtime.timeInMillis - System.currentTimeMillis()
    schedulealarm(context,startdelaymillis,action)
}


@SuppressLint("ScheduleExactAlarm")
fun schedulealarm(context: Context, delaymillis : Long, action : String)
{
    Log.i("MyAlarmReceiver","trying to schedule intent")
    var alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context,MyAlarmreceiver::class.java)
    intent.action = action
    if(action != AlarmConstants.END_ACTION_PERMANENT) {
        Log.i("MyAlarmReceiver", "scheduling ${action}")
        try {


            var pendingIntent =
                PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + delaymillis,
                pendingIntent
            )
            Log.i("MyAlarmReceiver","scheduled : ${action}")
        }
        catch (e:Exception)
        {
            Log.i("MyAlarmReceiver","got an exception : ${e}")
        }

    }
    else
    {
        try {


//            var pendingIntent =
//                PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
//            alarmManager.setExact(
//                AlarmManager.RTC_WAKEUP,
//                System.currentTimeMillis()+ 10000,
//                pendingIntent
//            )
            var contact = SharedPrefs(context!!).getString(
                "admincontact",
                CallStateRepository.defaultAdminContact
            )
            var viewModel = CallStateRepository.API_viewModel.value
            var dbhelper = CallStateRepository.databaseHelper

            var isnotcontact = contact.equals(CallStateRepository.defaultAdminContact)
            Toast.makeText(context,"Received Permanent Stop",Toast.LENGTH_LONG).show()
            if (isServiceRunning(context!!, Call_SMS_Service::class.java)) {
                Toast.makeText(context, "Stopping IVR System", Toast.LENGTH_SHORT).show()
                var patients = dbhelper!!.getAllPatients()
                if(patients.isNotEmpty())
                {
                    try {
                        CoroutineScope(Dispatchers.IO).launch {
                            viewModel!!.UpdatePatientsToBackend()
                        }

                    }
                    catch (e:Exception)
                    {
                        Log.e("DataSync","Error : ${e}")
                    }
                }
                CoroutineScope(Dispatchers.IO).launch {
                    CallStateRepository.isCallServiceRunning.collect{currentcall ->
                        if(currentcall != null)
                        {
                            currentcall.disconnect()

                            Log.e("DataSync","Stopped Permanently")
                        }
                        val stopServiceIntent = Intent(context, Call_SMS_Service::class.java)
                        context.stopService(stopServiceIntent)
                    }
                }
//                val stopServiceIntent = Intent(context, Call_SMS_Service::class.java)
//                context.stopService(stopServiceIntent)

                if (!isnotcontact) {
                    if (contact != null) {
                        sendSms(context, contact, AlarmConstants.ADMIN_MESSAGE_STOP_PERMANENT)
                    }

                }
            }
            Log.i("MyAlarmReceiver","scheduled : ${action}")
        }
        catch (e:Exception)
        {
            Log.i("MyAlarmReceiver","got an exception : ${e}")
        }

        Log.i("MyAlarmReceiver","scheduling end")
//        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)

    }


}
fun isServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
    val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val services = activityManager.getRunningServices(Integer.MAX_VALUE)
    for (service in services) {
        if (serviceClass.name == service.service.className) {
            return true
        }
    }
    return false
}