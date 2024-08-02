package com.germanautolabs.acaraus.main

import android.content.Context
import com.germanautolabs.acaraus.infra.Dispatchers
import com.germanautolabs.acaraus.infra.DispatchersApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.ksp.generated.module

@ComponentScan(value = "com.germanautolabs.acaraus")
@Module
class MainDi

fun setupMainDi(applicationContext: Context) = startKoin {
    androidLogger(Level.WARNING)
    androidContext(applicationContext)
    modules(
        MainDi().module,
        DispatchersDi().module,
    )
}

@Module
class DispatchersDi {
    @Single
    fun dispatchers(): Dispatchers = DispatchersApp()
}
