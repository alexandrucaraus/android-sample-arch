package com.germanautolabs.acaraus.main

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.ksp.generated.module

fun setupMainDi(applicationContext: Context) = startKoin {
    androidLogger(Level.WARNING)
    androidContext(applicationContext)
    modules(
        InfraDi().module,
        DataDi().module,
        UsecaseDi().module,
        ScreensDi().module,
    )
}

@Module
@ComponentScan(value = "com.germanautolabs.acaraus.infra")
class InfraDi

@Module
@ComponentScan(value = "com.germanautolabs.acaraus.data")
class DataDi

@Module
@ComponentScan(value = "com.germanautolabs.acaraus.usecase")
class UsecaseDi

@Module
@ComponentScan(value = "com.germanautolabs.acaraus.screens")
class ScreensDi
