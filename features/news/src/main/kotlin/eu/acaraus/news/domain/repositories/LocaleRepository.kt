package eu.acaraus.news.domain.repositories

interface LocaleRepository {
    fun setLanguageCode(code: String)
    fun getLanguageCode(): String
    fun setCountryCode(code: String)
    fun getCountryCode(): String
}
