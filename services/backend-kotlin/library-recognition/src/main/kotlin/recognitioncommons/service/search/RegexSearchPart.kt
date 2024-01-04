package recognitioncommons.service.search

import recognitioncommons.exception.SearchException
import recognitioncommons.models.Locale

// DO NOT SUPPORT LOCALE
class RegexSearchPart(
    val regex: Regex,
) : SearchPart {
    override fun contains(input: String): Boolean {
        return regex.containsMatchIn(input)
    }

    override fun containsLocale(input: String, locale: Locale): Boolean {
        return contains(input)
    }

    override fun findAndCut(input: String): String {
        regex.find(input) ?: throw SearchException(regex.pattern, input)
        return regex.replace(input, " ")
    }

    override fun findAndCutLocale(input: String, locale: Locale): String {
        return findAndCut(input)
    }
}
