package com.germanautolabs.acaraus.data

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent

fun launchBrowserCustomTab(activityContext: Context, url: String) {
    val uri = Uri.parse(url)
    val customTabsIntent = CustomTabsIntent.Builder()
        .setShowTitle(true)
        .build()
    customTabsIntent.launchUrl(activityContext, uri)
}
