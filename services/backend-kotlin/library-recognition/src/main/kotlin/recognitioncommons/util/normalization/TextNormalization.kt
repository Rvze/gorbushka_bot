package recognitioncommons.util.normalization

import recognitioncommons.models.Locale
import recognitioncommons.service.search.AnyMatchSearchPart
import recognitioncommons.service.search.SimpleSearchPart

private object TextNormalization {
    val lineTrimRegex = "\\s{2,}".toRegex()
    val specialSymbols = setOf('+', '.', ',')
    val activeSearchPart = AnyMatchSearchPart(
        setOf(
            SimpleSearchPart("актив", Locale.RU),
            SimpleSearchPart("active", Locale.EN),
        )
    )
}

fun customNormalizeText(text: String, filterer: (List<String>) -> List<String>): List<String> {
    return filterer(text.split("\n"))
        .map { it.replace(TextNormalization.lineTrimRegex, " ") }
        .filter { filterPredicate(it) }
}

fun normalizeText(text: String): List<String> {
    return text.split("\n")
        .map { it.replace(TextNormalization.lineTrimRegex, " ") }
        .filter { filterPredicate(it) }
}

private fun filterPredicate(line: String): Boolean {
    return notEmptyLinesPredicate(line)
        && notActiveIphonesPredicate(line)
}

private fun notActiveIphonesPredicate(line: String): Boolean {
    return !TextNormalization.activeSearchPart.contains(line)
}

private fun notEmptyLinesPredicate(line: String): Boolean {
    return line.isNotEmpty() &&
        line.isNotBlank()
}

internal fun filterInput(input: String): String {
    var filtered = ""
    for (i in input.indices) {
        val symbol = input[i]
        filtered += if (symbol.isLetterOrDigit() || symbol.isWhitespace() || symbol in TextNormalization.specialSymbols) {
            symbol
        } else {
            ' '
        }
    }
    return filtered.trim().replace("\\s+".toRegex(), " ")
}
