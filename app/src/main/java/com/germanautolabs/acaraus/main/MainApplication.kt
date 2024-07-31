package com.germanautolabs.acaraus.main

import android.app.Application

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        setupMainDi(this@MainApplication)
    }
}
