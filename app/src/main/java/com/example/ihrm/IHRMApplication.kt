package com.example.ihrm

import android.app.Application
import com.example.ihrm.util.AuthManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class IHRMApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AuthManager.init(this)
    }
}