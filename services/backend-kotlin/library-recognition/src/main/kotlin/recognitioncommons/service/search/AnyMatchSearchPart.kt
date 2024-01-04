package recognitioncommons.service.search

import recognitioncommons.exception.SearchException
import recognitioncommons.models.Locale

class AnyMatchSearchPart(
    private val searchParts: Set<SearchPart>,
) : SearchPart {
    override operator fun contains(input: String): Boolean {
        searchParts.forEach {
            if (it.contains(input)) {
                return true
            }
        }

        return false
    }

    override fun containsLocale(input: String, locale: Locale): Boolean {
        searchParts.forEach {
            if (it.containsLocale(input, locale)) {
                return true
            }
        }

        return false
    }

    override fun findAndCut(input: String): String {
        searchParts.forEach {
            try {
                return it.findAndCut(input)
            } catch (ignore: SearchException) {
            }
        }
        throw SearchException(searchParts.toString(), input)
    }

    override fun findAndCutLocale(input: String, locale: Locale): String {
        searchParts.forEach {
            try {
                return it.findAndCutLocale(input, locale)
            } catch (ignore: SearchException) {
            }
        }
        throw SearchException(searchParts.toString(), input)
    }
    fun containsAndReturn(input: String): SearchPart? {
        searchParts.forEach {
            if (it.contains(input)) {
                return it
            }
        }

        return null
    }
}
