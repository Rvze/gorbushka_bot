package recognitioncommons.service.search

import recognitioncommons.exception.SearchException
import recognitioncommons.models.Locale

class WhitespaceSearchPart(
    val locale: Locale,
    vararg parts: String,
) : SearchPart {
    private val regexSearchPart = build(parts)

    override fun contains(input: String): Boolean {
        return regexSearchPart.contains(input)
    }

    override fun containsLocale(input: String, locale: Locale): Boolean {
        return if (locale == this.locale || locale == Locale.NO || this.locale == Locale.NO) {
            contains(input)
        } else {
            false
        }
    }

    override fun findAndCut(input: String): String {
        return regexSearchPart.findAndCut(input)
    }

    override fun findAndCutLocale(input: String, locale: Locale): String {
        if (locale == this.locale || locale == Locale.NO || this.locale == Locale.NO) {
            return findAndCut(input)
        } else {
            throw SearchException(regexSearchPart.regex.pattern, input)
        }
    }

    private fun build(parts: Array<out String>): RegexSearchPart {
        return RegexSearchPart(parts.joinToString(separator = " *") {
            it
        }.toRegex(RegexOption.IGNORE_CASE))
    }
}
