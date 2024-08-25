package com.germanautolabs.acaraus.test.lib

import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

fun testViewModelStoreOwner() = object : ViewModelStoreOwner {
    override val viewModelStore: ViewModelStore get() = ViewModelStore()
}
