package recognitioncommons.service.recognition.apple.airpods

import recognitioncommons.exception.NotAirPodsException
import recognitioncommons.exception.staffRecognitionExceptionToHumanReadable
import recognitioncommons.models.apple.airpods.AirPods
import recognitioncommons.models.apple.airpods.AirPodsSearchModel
import recognitioncommons.models.country.Country
import recognitioncommons.service.recognition.apple.airpods.registry.AirPodsModelSearchParts
import recognitioncommons.service.recognition.common.recognizeAirPodsPrice
import recognitioncommons.service.recognition.common.recognizeCountries
import recognitioncommons.util.normalization.filterInput
import recognitioncommons.util.normalization.normalizeText

internal fun isAirpodsInLine(line: String): Boolean {
    return AirPodsModelSearchParts.AIRPODS_WORD_SEARCH_PART.contains(line)
}

fun recognizeAirPodsWithErrors(text: String): Pair<List<AirPods>, List<String>> {
    val airpods = mutableListOf<AirPods>()
    val errors = mutableListOf<String>()

    normalizeText(text).forEach { line ->
        try {
            val recognized = recognizeAirPods(line)
            airpods.addAll(recognized)
        } catch (e: NotAirPodsException) {
            val errorMessage = "${staffRecognitionExceptionToHumanReadable(e)}-$line"
            errors.add(errorMessage)
        }
    }
    return airpods to errors

}

internal fun recognizeAirPods(line: String): List<AirPods> {
    var inputTemp = line

    val countryResult = recognizeCountries(inputTemp)
    val countries = countryResult.first.let { it.ifEmpty { listOf(Country.USA) } }
    inputTemp = filterInput(countryResult.second)

    val modelResult = recognizeModel(inputTemp)
    val model = modelResult.first
    inputTemp = filterInput(modelResult.second)

    val priceResult = recognizeAirPodsPrice(inputTemp)
    val price = priceResult.first
    inputTemp = filterInput(priceResult.second)

    val colorResult = recognizeColor(inputTemp)
    val color = mapColor(model, colorResult.first)
    inputTemp = filterInput(colorResult.second)

    assertAirpodsColor(model, color)

    return countries.map {
        AirPods(
            country = it,
            color = color,
            price = price,
            model = model
        )
    }
}

internal fun recognizedAirPodsSearchModelsWithErrors(text: String): Pair<List<AirPodsSearchModel>, List<String>> {
    val airpods = mutableListOf<AirPodsSearchModel>()
    val errors = mutableListOf<String>()

    normalizeText(text).forEach { i ->
        try {
            val recognized = recognizeAirPodsSearchModels(i)
            airpods.addAll(recognized)
        } catch (e: NotAirPodsException) {
            val errorMsg = "${staffRecognitionExceptionToHumanReadable(e)} - $i"
            errors.add(errorMsg)
        }
    }
    return airpods to errors
}

internal fun recognizeAirPodsSearchModels(line: String): List<AirPodsSearchModel> {
    var inputTemp = line

    val countryResult = recognizeCountries(inputTemp)
    val countries = countryResult.first
    inputTemp = filterInput(countryResult.second)

    val color = try {
        val colorResult = recognizeColor(inputTemp, true)
        inputTemp = filterInput(colorResult.second)
        colorResult.first
    } catch (e: NotAirPodsException) {
        null
    }

    val modelResult = recognizeModel(inputTemp, true)
    inputTemp = filterInput(modelResult.second)
    val model = modelResult.first

    if (countries.isEmpty()) {
        return listOf(
            AirPodsSearchModel(
                color = color,
                model = model,
                country = null,
            )
        )
    }
    return countries.map {
        AirPodsSearchModel(
            color = color,
            model = model,
            country = it,
        )
    }
}
