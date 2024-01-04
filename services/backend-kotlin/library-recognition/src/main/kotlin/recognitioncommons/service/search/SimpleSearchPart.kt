package recognitioncommons.service.search

import recognitioncommons.exception.SearchException
import recognitioncommons.models.Locale

/**
 * Should contain exact
 * May be ignoring case
 */
class SimpleSearchPart(
    val toSearch: String,
    val locale: Locale = Locale.NO,
    val ignoreCase: Boolean = true,
) : SearchPart {
    override fun contains(input: String): Boolean {
        return input.contains(toSearch, ignoreCase)
    }

    override fun containsLocale(input: String, locale: Locale): Boolean {
        return if (locale == this.locale || locale == Locale.NO || this.locale == Locale.NO) {
            contains(input)
        } else {
            false
        }
    }

    override fun findAndCut(input: String): String {
        if (!contains(input)) {
            throw SearchException(toSearch, input)
        }

        return input.replaceFirst(
            oldValue = toSearch,
            newValue = " ",
            ignoreCase = ignoreCase,
        )
    }

    override fun findAndCutLocale(input: String, locale: Locale): String {
        if (locale == this.locale || locale == Locale.NO || this.locale == Locale.NO) {
            return findAndCut(input)
        } else {
            throw SearchException(toSearch, input)
        }
    }
}
