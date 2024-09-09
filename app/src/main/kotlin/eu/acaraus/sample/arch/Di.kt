package eu.acaraus.sample.arch

import android.content.Context
import eu.acaraus.news.data.remote.NewsApiConfig
import eu.acaraus.news.di.newsDiModules
import eu.acaraus.shared.lib.coroutines.DispatcherProvider
import eu.acaraus.shared.lib.coroutines.DispatcherProviderApp
import eu.acaraus.shared.lib.http.httpClient
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.ksp.generated.module

fun setupMainDi(applicationContext: Context) = startKoin {
    androidLogger(Level.WARNING)
    androidContext(applicationContext)
    modules(AppDi().module, *newsDiModules.toTypedArray())
}

@Module
class AppDi {

    @Single
    fun provideDispatcherProvider(): DispatcherProvider = DispatcherProviderApp()

    @Factory
    @Named(DispatcherProvider.VM_COROUTINE_SCOPE)
    fun provideViewModelCoroutineScope(
        dispatcherProvider: DispatcherProvider,
    ): CoroutineScope = dispatcherProvider.vmCoroutineScope()

    @Single
    fun provideHttpClient(): HttpClient = httpClient()

    @Single
    fun provideNewsApiConfig() = NewsApiConfig(
        baseUrl = BuildConfig.NEWS_BASE_URL,
        apiKey = BuildConfig.NEWS_API_KEY,
    )
}
