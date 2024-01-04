package recognitioncommons.service.search

import recognitioncommons.models.Locale

interface SearchPart {
    fun contains(input: String): Boolean

    fun containsLocale(input: String, locale: Locale): Boolean

    fun findAndCut(input: String): String

    fun findAndCutLocale(input: String, locale: Locale): String
}
