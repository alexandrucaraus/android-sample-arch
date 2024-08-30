package eu.acaraus.news.domain.repositories

interface LocaleStore {
    fun setLanguageCode(code: String)
    fun getLanguageCode(): String
    fun setCountryCode(code: String)
    fun getCountryCode(): String
}
