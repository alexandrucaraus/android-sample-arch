package eu.acaraus.news.test.snapshots

import androidx.compose.runtime.currentComposer
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import eu.acaraus.news.test.rules.UTest
import eu.acaraus.shared.lib.coroutines.DispatcherProvider
import eu.acaraus.shared.test.lib.previews.getAListOfAllComposablePreviewMethods
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.component.inject

class SnapshotTest : UTest {

    @get:Rule
    val paparazzi = Paparazzi(
        theme = "android:Theme.Material.Light.NoActionBar",
        deviceConfig = DeviceConfig.PIXEL_6_PRO,
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        val dispatcherProvider by inject<DispatcherProvider>()
        Dispatchers.setMain(dispatcherProvider.ui)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

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
