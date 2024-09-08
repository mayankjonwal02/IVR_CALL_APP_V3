package com.example.ivr_call_app_v3.android.FunctionalComponents.NetworkCalls

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private var retrofit : Retrofit? = null

    fun getclient(baseurl : String): Retrofit? {
        if(retrofit == null || retrofit!!.baseUrl().toString() != baseurl)
        {
            retrofit = Retrofit.Builder()
                .baseUrl(baseurl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        return retrofit
    }

}