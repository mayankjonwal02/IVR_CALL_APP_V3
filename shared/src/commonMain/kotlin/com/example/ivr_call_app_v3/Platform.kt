package com.example.ivr_call_app_v3

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform