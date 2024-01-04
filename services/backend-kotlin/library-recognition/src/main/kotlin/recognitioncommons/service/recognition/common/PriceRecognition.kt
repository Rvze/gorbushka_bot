package recognitioncommons.service.recognition.common

import recognitioncommons.exception.AirPodsPriceIsAbsentException
import recognitioncommons.exception.IphonePriceIsAbsentException
import recognitioncommons.exception.MacbookPriceIsAbsentException

val iphonePriceRegex = Regex("\\d{2,}[., ]?(\\d){3}")
fun recognizeIphonePrice(input: String): Pair<Int, String> {
    var found = iphonePriceRegex.find(input) ?: throw IphonePriceIsAbsentException(input)
    while (found.next() != null) {
        found = found.next()!!
    }

    try {
        return found.value.filter { it in '0'..'9' }.toInt() to
            "${input.subSequence(0, found.range.first)}${input.subSequence(found.range.last + 1, input.length)}"
    } catch (e: Exception) {
        throw IphonePriceIsAbsentException(input)
    }
}

fun recognizeMacbookPrice(input: String): Pair<Int, String> {
    try {
        return recognizeIphonePrice(input)
    } catch (e: IphonePriceIsAbsentException) {
        throw MacbookPriceIsAbsentException(input)
    }
}

val airPodsPriceRegex = Regex("\\d+[., ]?(\\d){2}0")
fun recognizeAirPodsPrice(input: String): Pair<Int, String> {
    var found = airPodsPriceRegex.find(input) ?: throw AirPodsPriceIsAbsentException(input)
    while (found.next() != null) {
        found = found.next()!!
    }

    try {
        return found.value.filter { it in '0'..'9' }.toInt() to
            "${input.subSequence(0, found.range.first)}${input.subSequence(found.range.last + 1, input.length)}"
    } catch (e: Exception) {
        throw AirPodsPriceIsAbsentException(input)
    }
}
