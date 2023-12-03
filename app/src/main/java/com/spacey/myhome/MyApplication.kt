package com.spacey.myhome

import android.app.Application
import com.spacey.data.AppComponent

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppComponent.initiate(this)
    }
}