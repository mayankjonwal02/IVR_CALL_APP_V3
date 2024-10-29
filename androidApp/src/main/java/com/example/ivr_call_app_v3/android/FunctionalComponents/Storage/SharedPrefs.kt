package com.example.ivr_call_app_v3.android.FunctionalComponents.Storage

import android.content.Context
import android.content.SharedPreferences

class SharedPrefs(context: Context)
{
    private val sharedPrefs : SharedPreferences = context.getSharedPreferences("IVR_Shared_Prefs",Context.MODE_PRIVATE)

    fun add(itemname:String,item:String)
    {
        sharedPrefs.edit().putString(itemname,item).apply()
    }

    fun addInt(itemname:String,item:Int)
    {
        sharedPrefs.edit().putInt(itemname,item).apply()
    }
    fun getString(itemname: String, default:String): String? {
        return sharedPrefs.getString(itemname,default)
    }

    fun getInt(itemname: String, default: Int) : Int?
    {
        return sharedPrefs.getInt(itemname,default)
    }

}