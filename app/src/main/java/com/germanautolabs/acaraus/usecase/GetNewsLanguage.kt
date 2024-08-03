package com.germanautolabs.acaraus.usecase

import org.koin.core.annotation.Factory

@Factory
class GetNewsLanguage {

    private val newsLanguages = mapOf(
        "ar" to "Arabic",
        "de" to "German",
        "en" to "English",
        "es" to "Spanish",
        "fr" to "French",
        "he" to "Hebrew",
        "it" to "Italian",
        "nl" to "Dutch",
        "no" to "Norwegian",
        "pt" to "Portuguese",
        "ru" to "Russian",
        "sv" to "Swedish",
        "ud" to "Undefined",
        "zh" to "Chinese",
    )

    fun options() = newsLanguages.values.toSet()

    fun getLanguageCodeByName(languageName: String): String =
        newsLanguages.getKeyByValue(languageName) ?: "English"

    private fun <K, V> Map<K, V>.getKeyByValue(value: V): K? {
        return this.entries.firstOrNull { it.value == value }?.key
    }
}
