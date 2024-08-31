package eu.acaraus.sample.arch

import android.app.Application

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        setupMainDi(this@Application)
    }
}
