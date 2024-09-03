package eu.acaraus.shared.lib.android

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.browser.customtabs.CustomTabsIntent
import androidx.navigation.NavType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import java.net.URLEncoder

inline fun <reified T : Any> serializableNavType(
    isNullableAllowed: Boolean = false,
    json: Json = Json,
) = object : NavType<T>(isNullableAllowed = isNullableAllowed) {
    override fun get(bundle: Bundle, key: String) =
        bundle.getString(key)?.let<String, T>(json::decodeFromString)

    override fun parseValue(value: String): T =
        json.decodeFromString(URLDecoder.decode(value, "UTF-8"))

    override fun serializeAsValue(value: T): String =
        URLEncoder.encode(json.encodeToString(value), "UTF-8")

    override fun put(bundle: Bundle, key: String, value: T) {
        bundle.putString(key, json.encodeToString(value))
    }
}

fun Context.launchBrowserUrl(url: String) {
    val uri = Uri.parse(url)
    val customTabsIntent = CustomTabsIntent.Builder().setShowTitle(true).build()
    customTabsIntent.launchUrl(this, uri)
}
