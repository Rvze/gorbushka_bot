package recognitioncommons.service.search

import recognitioncommons.models.Locale

/**
 * Should contain all parts
 */
class AllMatchSearchPart(
    var searchParts: Set<SearchPart>,
) : SearchPart {
    override fun contains(input: String): Boolean {
        searchParts.forEach {
            if (!it.contains(input)) {
                return false
            }
        }

        return true
    }

    override fun containsLocale(input: String, locale: Locale): Boolean {
        searchParts.forEach {
            if (!it.containsLocale(input, locale)) {
                return false
            }
        }

        return true
    }

    override fun findAndCut(input: String): String {
        var cur = input
        searchParts.forEach {
            cur = it.findAndCut(cur)
        }
        return cur
    }

    override fun findAndCutLocale(input: String, locale: Locale): String {
        var cur = input
        searchParts.forEach {
            cur = it.findAndCutLocale(cur, locale)
        }
        return cur
    }
}
