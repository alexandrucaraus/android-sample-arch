package com.germanautolabs.acaraus.data

import org.koin.core.annotation.Single
import java.util.Locale

interface LocaleStore {
    fun setLanguageCode(code: String)
    fun getLanguageCode(): String
    fun setCountryCode(code: String)
    fun getCountryCode(): String
}

@Single(binds = [LocaleStore::class])
class LocaleStoreImpl : LocaleStore {

    private var languageCode = Locale.getDefault().language
    private var countryCode = Locale.getDefault().country

    @Synchronized
    override fun setLanguageCode(code: String) {
        languageCode = code
    }

    @Synchronized
    override fun getLanguageCode(): String = languageCode

    @Synchronized
    override fun setCountryCode(code: String) {
        countryCode = code
    }

    @Synchronized
    override fun getCountryCode(): String = countryCode
}
