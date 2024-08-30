package eu.acaraus.news

import android.content.Context
import eu.acaraus.news.di.newsDiModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

fun setupMainDi(applicationContext: Context) = startKoin {
    androidLogger(Level.WARNING)
    androidContext(applicationContext)
    modules(*newsDiModules.toTypedArray())
}
