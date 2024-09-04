package eu.acaraus.news.test.snapshots

import androidx.compose.runtime.currentComposer
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import eu.acaraus.news.test.rules.UTest
import eu.acaraus.shared.lib.coroutines.DispatcherProvider
import eu.acaraus.shared.lib.coroutines.DispatcherProviderApp
import eu.acaraus.shared.test.lib.previews.getAListOfAllComposablePreviewMethods
import org.junit.Rule
import org.junit.Test
import org.koin.core.module.Module
import org.koin.dsl.module

class SnapshotTest : UTest {

    override fun perTestModules(): Array<Module> =
        arrayOf(module { single<DispatcherProvider> { DispatcherProviderApp() } })

    @get:Rule
    val paparazzi = Paparazzi(
        theme = "android:Theme.Material.Light.NoActionBar",
        deviceConfig = DeviceConfig.PIXEL_6_PRO,
    )

    @Test
    fun generateSnapshot() {
        "eu.acaraus.news.presentation"
            .getAListOfAllComposablePreviewMethods()
            .forEach { method ->
                println(method)
                paparazzi.snapshot(name = method.name) {
                    method.invoke(null, currentComposer, 0)
                }
            }
    }
}
