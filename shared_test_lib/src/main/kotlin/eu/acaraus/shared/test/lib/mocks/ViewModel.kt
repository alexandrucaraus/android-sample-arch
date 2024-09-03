package eu.acaraus.shared.test.lib.mocks

import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

fun testViewModelStoreOwner() = object : ViewModelStoreOwner {
    override val viewModelStore: ViewModelStore get() = ViewModelStore()
}
