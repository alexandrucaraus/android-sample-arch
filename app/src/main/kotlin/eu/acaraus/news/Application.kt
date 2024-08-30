package eu.acaraus.news

import android.app.Application

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        setupMainDi(this@Application)
    }
}
