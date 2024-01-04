package recognitioncommons.service.recognition.common

import recognitioncommons.models.country.Country
import recognitioncommons.models.country.CountryFlagEmojis

fun recognizeCountries(input: String): Pair<List<Country>, String> {
    val unicodesToRemove = mutableListOf<String>()
    val foundCountries = CountryFlagEmojis.emojiUnicodeToCountry.entries.filter {
        input.contains(it.key, true) ||
                input.contains(it.value.name, true) ||
                input.contains(it.value.nameRu, true)
    }.map {
        unicodesToRemove.add(it.key)
        it.value
    }

    var filteredInput = input
    unicodesToRemove.forEach {
        filteredInput = filteredInput.replace(it, " ")
    }

    return foundCountries to filteredInput
}

fun recognizeOneCountry(input: String): Pair<Country?, String> {
    CountryFlagEmojis.emojiUnicodeToCountry.entries.forEach {
        if (input.contains(it.key, true)) {
            return it.value to input.replace(it.key, " ")
        }

        if (input.contains(it.value.name, true)) {
            return it.value to input.replace(it.value.name, " ")
        }

        if (input.contains(it.value.nameRu, true)) {
            return it.value to input.replace(it.value.nameRu, " ")
        }
    }
    return null to input
}
