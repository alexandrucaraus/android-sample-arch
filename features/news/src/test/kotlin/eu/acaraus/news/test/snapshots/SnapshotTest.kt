package eu.acaraus.news.test.snapshots

import androidx.compose.runtime.currentComposer
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import eu.acaraus.news.test.common.UnitTest
import eu.acaraus.news.test.common.previews.getAListOfAllComposablePreviewMethods
import org.junit.Rule
import org.junit.Test

class SnapshotTest : UnitTest {

    @get:Rule
    val paparazzi = Paparazzi(
        theme = "android:Theme.Material.Light.NoActionBar",
        deviceConfig = DeviceConfig.PIXEL_6_PRO,
    )

    @Test
    fun generateSnapshot() {
        "eu.acaraus.news.presentation"
            .getAListOfAllComposablePreviewMethods()
            .forEach { method -> paparazzi.snapshot { method.invoke(null, currentComposer, 0) } }
    }
}
