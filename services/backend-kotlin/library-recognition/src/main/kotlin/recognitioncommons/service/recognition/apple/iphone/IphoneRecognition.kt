package recognitioncommons.service.recognition.apple.iphone

import recognitioncommons.exception.NotIphoneException
import recognitioncommons.exception.staffRecognitionExceptionToHumanReadable
import recognitioncommons.models.apple.iphone.Iphone
import recognitioncommons.models.apple.iphone.IphoneSearchModel
import recognitioncommons.util.normalization.filterInput
import recognitioncommons.util.normalization.normalizeText
import recognitioncommons.service.recognition.common.recognizeCountries
import recognitioncommons.service.recognition.common.recognizeIphonePrice

fun recognizeIphonesWithErrors(text: String): Pair<List<Iphone>, List<String>> {
    val iphones = mutableListOf<Iphone>()
    val errors = mutableListOf<String>()

    normalizeText(text)
        .forEach { line ->
            try {
                val recognized = recognizeIphones(line)
                iphones.addAll(recognized)
            } catch (e: NotIphoneException) {
                val msg = "${staffRecognitionExceptionToHumanReadable(e)} - $line"
                errors.add(msg)
            }
        }

    return iphones to errors
}

internal fun recognizeIphones(line: String): List<Iphone> {
    var inputTemp = line

    val countryResult = recognizeCountries(inputTemp)
    val countries = countryResult.first
    inputTemp = filterInput(countryResult.second)

    val priceResult = recognizeIphonePrice(inputTemp)
    val price = priceResult.first
    inputTemp = filterInput(priceResult.second)

    val colorResult = recognizeColor(inputTemp)
    val color = colorResult.first
    inputTemp = filterInput(colorResult.second)

    val memoryResult = recognizeMemory(inputTemp)
    val memory = memoryResult.first
    inputTemp = filterInput(memoryResult.second)

    val modelResult = recognizeModel(input = inputTemp)
    val model = modelResult.first
    assertIphoneMemory(model = model, memory = memory)

    return countries.map {
        Iphone(
            model = model,
            color = mapColor(model, color),
            memory = memory,
            price = price,
            country = it,
        )
    }
}

internal fun recognizeIphoneSearchModelsWithErrors(text: String): Pair<List<IphoneSearchModel>, List<String>> {
    val iphones = mutableListOf<IphoneSearchModel>()
    val errors = mutableListOf<String>()

    normalizeText(text)
        .forEach { line ->
            try {
                val recognized = recognizeIphoneSearchModels(line)
                iphones.addAll(recognized)
            } catch (e: NotIphoneException) {
                val msg = "$line - ${staffRecognitionExceptionToHumanReadable(e)}"
                errors.add(msg)
            }
        }

    return iphones to errors
}

internal fun recognizeIphoneSearchModels(line: String): List<IphoneSearchModel> {
    var inputTemp = line

    val countryResult = recognizeCountries(inputTemp)
    val countries = countryResult.first
    inputTemp = filterInput(countryResult.second)

    val color = try {
        val colorResult = recognizeColor(inputTemp, true)
        inputTemp = filterInput(colorResult.second)
        colorResult.first
    } catch (e: NotIphoneException) {
        null
    }

    val memory = try {
        val memoryResult = recognizeMemory(inputTemp)
        inputTemp = filterInput(memoryResult.second)
        memoryResult.first
    } catch (e: NotIphoneException) {
        null
    }

    val modelResult = recognizeModel(inputTemp, true)
    val model = modelResult.first
    memory?.let { assertIphoneMemory(memory = it, model = model) }
    inputTemp = modelResult.second

    if (countries.isEmpty()) {
        return listOf(
            IphoneSearchModel(
                model = model,
                memory = memory,
                color = color?.let { mapColor(model, it) },
                country = null,
            )
        )
    }

    return countries.map {
        IphoneSearchModel(
            model = model,
            memory = memory,
            color = color?.let { clr -> mapColor(model, clr) },
            country = it,
        )
    }
}
