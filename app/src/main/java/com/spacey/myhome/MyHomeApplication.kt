package com.spacey.myhome

import android.app.Application
import com.spacey.myhome.data.DIServiceLocator

class MyHomeApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        DIServiceLocator.initializeApp(applicationContext)
    }
}