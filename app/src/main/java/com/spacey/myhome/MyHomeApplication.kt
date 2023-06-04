package com.spacey.myhome

import android.app.Application
import com.example.data.DIServiceLocator

class MyHomeApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        DIServiceLocator.initializeApp(applicationContext)
    }
}